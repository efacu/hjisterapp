import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Hjisterapp1SharedModule } from 'app/shared/shared.module';
import { UbicacionComponent } from './ubicacion.component';
import { UbicacionDetailComponent } from './ubicacion-detail.component';
import { UbicacionUpdateComponent } from './ubicacion-update.component';
import { UbicacionDeletePopupComponent, UbicacionDeleteDialogComponent } from './ubicacion-delete-dialog.component';
import { ubicacionRoute, ubicacionPopupRoute } from './ubicacion.route';

const ENTITY_STATES = [...ubicacionRoute, ...ubicacionPopupRoute];

@NgModule({
  imports: [Hjisterapp1SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UbicacionComponent,
    UbicacionDetailComponent,
    UbicacionUpdateComponent,
    UbicacionDeleteDialogComponent,
    UbicacionDeletePopupComponent
  ],
  entryComponents: [UbicacionComponent, UbicacionUpdateComponent, UbicacionDeleteDialogComponent, UbicacionDeletePopupComponent]
})
export class Hjisterapp1UbicacionModule {}
