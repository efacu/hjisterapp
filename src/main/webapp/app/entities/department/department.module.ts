import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Hjisterapp1SharedModule } from 'app/shared/shared.module';
import { DepartmentComponent } from './department.component';
import { DepartmentDetailComponent } from './department-detail.component';
import { DepartmentUpdateComponent } from './department-update.component';
import { DepartmentDeletePopupComponent, DepartmentDeleteDialogComponent } from './department-delete-dialog.component';
import { departmentRoute, departmentPopupRoute } from './department.route';

const ENTITY_STATES = [...departmentRoute, ...departmentPopupRoute];

@NgModule({
  imports: [Hjisterapp1SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    DepartmentComponent,
    DepartmentDetailComponent,
    DepartmentUpdateComponent,
    DepartmentDeleteDialogComponent,
    DepartmentDeletePopupComponent
  ],
  entryComponents: [DepartmentComponent, DepartmentUpdateComponent, DepartmentDeleteDialogComponent, DepartmentDeletePopupComponent]
})
export class Hjisterapp1DepartmentModule {}
