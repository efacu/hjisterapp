import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Hjisterapp1TestModule } from '../../../test.module';
import { ProvinciaComponent } from 'app/entities/provincia/provincia.component';
import { ProvinciaService } from 'app/entities/provincia/provincia.service';
import { Provincia } from 'app/shared/model/provincia.model';

describe('Component Tests', () => {
  describe('Provincia Management Component', () => {
    let comp: ProvinciaComponent;
    let fixture: ComponentFixture<ProvinciaComponent>;
    let service: ProvinciaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Hjisterapp1TestModule],
        declarations: [ProvinciaComponent],
        providers: []
      })
        .overrideTemplate(ProvinciaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProvinciaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ProvinciaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Provincia(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.provincias[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
