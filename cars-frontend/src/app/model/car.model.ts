import { Brand } from './brand.model';

export class Car {
  idCar!: number;
  model!: string;
  price!: number;
  releaseDate!: Date;
  licensePlate!: string;
  imagePath!: string;
  brand!: Brand;
}
