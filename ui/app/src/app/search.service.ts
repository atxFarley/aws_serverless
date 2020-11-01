import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';

import {environment} from './../environments/environment';

declare var leafletMap: any;

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrl = environment.apiURL;

  searchFields(searchboxValue: string): Observable<void> {
    console.log('search service searchFields(' + searchboxValue + ')');
    leafletMap.searchFields(searchboxValue);
    return of();

    // maybe add another search here for json
  }

  constructor() {
  }
}
