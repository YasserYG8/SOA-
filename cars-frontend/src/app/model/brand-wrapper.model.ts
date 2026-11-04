import { Brand } from './brand.model';

// Spring Data REST returns HAL: the list is wrapped in _embedded.
export class BrandWrapper {
  _embedded!: { brands: Brand[] };
}
