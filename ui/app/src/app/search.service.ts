import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
declare var leafletMap: any;

@Injectable({
  providedIn: 'root'
})
export class SearchService {


  searchFields(searchboxValue: string) : Observable<void> {
    console.log("search service searchFields(" + searchboxValue + ")");
    leafletMap.searchFields(searchboxValue);
    return of();

    //maybe add another search here for json
  }

  constructor() { }
}
