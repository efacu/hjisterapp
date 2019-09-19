import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        loadChildren: () => import('./region/region.module').then(m => m.Hjisterapp1RegionModule)
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.Hjisterapp1CountryModule)
      },
      {
        path: 'provincia',
        loadChildren: () => import('./provincia/provincia.module').then(m => m.Hjisterapp1ProvinciaModule)
      },
      {
        path: 'ubicacion',
        loadChildren: () => import('./ubicacion/ubicacion.module').then(m => m.Hjisterapp1UbicacionModule)
      },
      {
        path: 'department',
        loadChildren: () => import('./department/department.module').then(m => m.Hjisterapp1DepartmentModule)
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.Hjisterapp1TaskModule)
      },
      {
        path: 'employee',
        loadChildren: () => import('./employee/employee.module').then(m => m.Hjisterapp1EmployeeModule)
      },
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.Hjisterapp1JobModule)
      },
      {
        path: 'job-history',
        loadChildren: () => import('./job-history/job-history.module').then(m => m.Hjisterapp1JobHistoryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: []
})
export class Hjisterapp1EntityModule {}
