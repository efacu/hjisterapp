import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Hjisterapp1TestModule } from '../../../test.module';
import { UbicacionComponent } from 'app/entities/ubicacion/ubicacion.component';
import { UbicacionService } from 'app/entities/ubicacion/ubicacion.service';
import { Ubicacion } from 'app/shared/model/ubicacion.model';

describe('Component Tests', () => {
  describe('Ubicacion Management Component', () => {
    let comp: UbicacionComponent;
    let fixture: ComponentFixture<UbicacionComponent>;
    let service: UbicacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Hjisterapp1TestModule],
        declarations: [UbicacionComponent],
        providers: []
      })
        .overrideTemplate(UbicacionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UbicacionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UbicacionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Ubicacion(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ubicacions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
