import { Pipe, PipeTransform } from '@angular/core';
import { Car } from '../model/car.model';

@Pipe({
  name: 'searchFilter',
  standalone: true
})
export class SearchFilterPipe implements PipeTransform {
  transform(list: Car[], searchText: string): Car[] {
    const q = (searchText ?? '').trim().toLowerCase();
    if (!q) {
      return list;
    }
    return list.filter(c => c.model.toLowerCase().includes(q));
  }
}
