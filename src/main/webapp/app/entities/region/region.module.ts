import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Hjisterapp1SharedModule } from 'app/shared/shared.module';
import { RegionComponent } from './region.component';
import { RegionDetailComponent } from './region-detail.component';
import { RegionUpdateComponent } from './region-update.component';
import { RegionDeletePopupComponent, RegionDeleteDialogComponent } from './region-delete-dialog.component';
import { regionRoute, regionPopupRoute } from './region.route';

const ENTITY_STATES = [...regionRoute, ...regionPopupRoute];

@NgModule({
  imports: [Hjisterapp1SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [RegionComponent, RegionDetailComponent, RegionUpdateComponent, RegionDeleteDialogComponent, RegionDeletePopupComponent],
  entryComponents: [RegionComponent, RegionUpdateComponent, RegionDeleteDialogComponent, RegionDeletePopupComponent]
})
export class Hjisterapp1RegionModule {}
