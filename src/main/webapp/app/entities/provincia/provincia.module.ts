import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Hjisterapp1SharedModule } from 'app/shared/shared.module';
import { ProvinciaComponent } from './provincia.component';
import { ProvinciaDetailComponent } from './provincia-detail.component';
import { ProvinciaUpdateComponent } from './provincia-update.component';
import { ProvinciaDeletePopupComponent, ProvinciaDeleteDialogComponent } from './provincia-delete-dialog.component';
import { provinciaRoute, provinciaPopupRoute } from './provincia.route';

const ENTITY_STATES = [...provinciaRoute, ...provinciaPopupRoute];

@NgModule({
  imports: [Hjisterapp1SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProvinciaComponent,
    ProvinciaDetailComponent,
    ProvinciaUpdateComponent,
    ProvinciaDeleteDialogComponent,
    ProvinciaDeletePopupComponent
  ],
  entryComponents: [ProvinciaComponent, ProvinciaUpdateComponent, ProvinciaDeleteDialogComponent, ProvinciaDeletePopupComponent]
})
export class Hjisterapp1ProvinciaModule {}
