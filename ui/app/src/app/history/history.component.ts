import {Component, OnInit, Input} from '@angular/core';
import {Field} from '../field';
import {FieldAttribute} from "../fieldAttribute";
import {FieldActivity} from "../fieldActivity";
import {FieldHistory} from "../fieldHistory";

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
    fieldName: "Test Field",
    growerName: "Amy Farley",
    ownerName: "",
    acres: 40,
    addressStreet: "",
    addressCity: "",
    addressState: "",
    addressZip: "",
    addressCounty: "",
    addressDesc: "",
    attributes: this.attributes,
    activities: this.activities,
    history: this.history
  };

  constructor() {
  }

  ngOnInit() {
  }


}
