import {Component, OnInit, Input} from '@angular/core';
import {Field} from '../field';
import {FieldAttribute} from '../fieldAttribute';
import {FieldActivity} from '../fieldActivity';
import {FieldHistory} from '../fieldHistory';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  attributes: FieldAttribute[] = [];
  activities: FieldActivity[] = [];
  history: FieldHistory[] = [];

  field: Field = {
    fieldId: 1,
    fieldName: 'Test Field',
    fieldDesc: 'This is a field description',
    growerName: 'Amy Farley',
    growerId: 1,
    ownerName: '',
    ownerId: 1,
    acres: 40,
    addressStreet: '',
    addressCity: '',
    addressState: '',
    addressZip: '',
    addressCounty: '',
    addressDesc: '',
    fieldAttributes: this.attributes,
    fieldActivities: this.activities,
    fieldHistory: this.history
  };

  constructor() {
  }

  ngOnInit() {
  }


}
