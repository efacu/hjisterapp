import { IProvincia } from 'app/shared/model/provincia.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IUbicacion {
  id?: number;
  calle?: string;
  codigoPostal?: string;
  ciudad?: string;
  stateProvince?: string;
  provincia?: IProvincia;
  nombreUbicacion?: IDepartment;
}

export class Ubicacion implements IUbicacion {
  constructor(
    public id?: number,
    public calle?: string,
    public codigoPostal?: string,
    public ciudad?: string,
    public stateProvince?: string,
    public provincia?: IProvincia,
    public nombreUbicacion?: IDepartment
  ) {}
}
