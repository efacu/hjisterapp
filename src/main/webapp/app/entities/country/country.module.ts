import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Hjisterapp1SharedModule } from 'app/shared/shared.module';
import { CountryComponent } from './country.component';
import { CountryDetailComponent } from './country-detail.component';
import { CountryUpdateComponent } from './country-update.component';
import { CountryDeletePopupComponent, CountryDeleteDialogComponent } from './country-delete-dialog.component';
import { countryRoute, countryPopupRoute } from './country.route';

const ENTITY_STATES = [...countryRoute, ...countryPopupRoute];

@NgModule({
  imports: [Hjisterapp1SharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    CountryComponent,
    CountryDetailComponent,
    CountryUpdateComponent,
    CountryDeleteDialogComponent,
    CountryDeletePopupComponent
  ],
  entryComponents: [CountryComponent, CountryUpdateComponent, CountryDeleteDialogComponent, CountryDeletePopupComponent]
})
export class Hjisterapp1CountryModule {}
