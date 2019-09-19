import { ICountry } from 'app/shared/model/country.model';
import { IUbicacion } from 'app/shared/model/ubicacion.model';

export interface IProvincia {
  id?: number;
  nombreProvincia?: string;
  country?: ICountry;
  nombreProvincias?: IUbicacion[];
}

export class Provincia implements IProvincia {
  constructor(public id?: number, public nombreProvincia?: string, public country?: ICountry, public nombreProvincias?: IUbicacion[]) {}
}
