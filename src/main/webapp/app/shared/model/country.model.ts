import { IProvincia } from 'app/shared/model/provincia.model';

export interface ICountry {
  id?: number;
  countryName?: string;
  nombrePais?: IProvincia[];
}

export class Country implements ICountry {
  constructor(public id?: number, public countryName?: string, public nombrePais?: IProvincia[]) {}
}
