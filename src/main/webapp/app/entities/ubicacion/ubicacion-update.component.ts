import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUbicacion, Ubicacion } from 'app/shared/model/ubicacion.model';
import { UbicacionService } from './ubicacion.service';
import { IProvincia } from 'app/shared/model/provincia.model';
import { ProvinciaService } from 'app/entities/provincia/provincia.service';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department/department.service';

@Component({
  selector: 'jhi-ubicacion-update',
  templateUrl: './ubicacion-update.component.html'
})
export class UbicacionUpdateComponent implements OnInit {
  isSaving: boolean;

  provincias: IProvincia[];

  nombreubicacions: IDepartment[];

  editForm = this.fb.group({
    id: [],
    calle: [],
    codigoPostal: [],
    ciudad: [],
    stateProvince: [],
    provincia: [],
    nombreUbicacion: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected ubicacionService: UbicacionService,
    protected provinciaService: ProvinciaService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ubicacion }) => {
      this.updateForm(ubicacion);
    });
    this.provinciaService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProvincia[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProvincia[]>) => response.body)
      )
      .subscribe((res: IProvincia[]) => (this.provincias = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.departmentService
      .query({ filter: 'ubicacion-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IDepartment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDepartment[]>) => response.body)
      )
      .subscribe(
        (res: IDepartment[]) => {
          if (!this.editForm.get('nombreUbicacion').value || !this.editForm.get('nombreUbicacion').value.id) {
            this.nombreubicacions = res;
          } else {
            this.departmentService
              .find(this.editForm.get('nombreUbicacion').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IDepartment>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IDepartment>) => subResponse.body)
              )
              .subscribe(
                (subRes: IDepartment) => (this.nombreubicacions = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(ubicacion: IUbicacion) {
    this.editForm.patchValue({
      id: ubicacion.id,
      calle: ubicacion.calle,
      codigoPostal: ubicacion.codigoPostal,
      ciudad: ubicacion.ciudad,
      stateProvince: ubicacion.stateProvince,
      provincia: ubicacion.provincia,
      nombreUbicacion: ubicacion.nombreUbicacion
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ubicacion = this.createFromForm();
    if (ubicacion.id !== undefined) {
      this.subscribeToSaveResponse(this.ubicacionService.update(ubicacion));
    } else {
      this.subscribeToSaveResponse(this.ubicacionService.create(ubicacion));
    }
  }

  private createFromForm(): IUbicacion {
    return {
      ...new Ubicacion(),
      id: this.editForm.get(['id']).value,
      calle: this.editForm.get(['calle']).value,
      codigoPostal: this.editForm.get(['codigoPostal']).value,
      ciudad: this.editForm.get(['ciudad']).value,
      stateProvince: this.editForm.get(['stateProvince']).value,
      provincia: this.editForm.get(['provincia']).value,
      nombreUbicacion: this.editForm.get(['nombreUbicacion']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUbicacion>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackProvinciaById(index: number, item: IProvincia) {
    return item.id;
  }

  trackDepartmentById(index: number, item: IDepartment) {
    return item.id;
  }
}
