import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUbicacion } from 'app/shared/model/ubicacion.model';

@Component({
  selector: 'jhi-ubicacion-detail',
  templateUrl: './ubicacion-detail.component.html'
})
export class UbicacionDetailComponent implements OnInit {
  ubicacion: IUbicacion;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ubicacion }) => {
      this.ubicacion = ubicacion;
    });
  }

  previousState() {
    window.history.back();
  }
}
