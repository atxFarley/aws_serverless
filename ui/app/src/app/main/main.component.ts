import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';

import {Field} from '../field';
import {FieldUser} from "../fieldUser";
import {SearchService} from '../search.service';
import {FieldService} from "../field.service";
import {FielduserService} from "../fielduser.service";
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';

declare var leafletMap: any;

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit, OnChanges {
  @Input() searchVal: String;

  searchResults: Field[] = [];
  selectedField: Field;
  fieldUsers: FieldUser[];
  recentSearchBoxValue: string;
  @Input() selectedFieldId: number;


  constructor(private searchService: SearchService, private route: ActivatedRoute,
              private location: Location, private fieldService: FieldService, private fieldUserService: FielduserService) {


  }

  ngOnInit(): void {
    leafletMap.init();
    this.getFieldUsers();

  }

  ngOnChanges(changes: SimpleChanges): void {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'selectedFieldId': {

          }
        }
      }
    }
  }

  getFieldUsers(): void {
    this.fieldUserService.getFieldUsers()
      .subscribe(fieldUsers => this.fieldUsers = fieldUsers);
  }


  searchFields(searchboxValue: string): void {
    console.log("searchboxValue: " + searchboxValue);
    this.recentSearchBoxValue  = searchboxValue;
    this.searchService.searchFields(searchboxValue)
      .subscribe(searchVal => {
        this.searchVal = searchboxValue
        // do something; without data it's just an event
      });
  }


  getFieldDetails(): void {
    let value = this.selectedFieldId;
    let element: HTMLInputElement = document.getElementById("fieldID") as HTMLInputElement;
    value = parseInt(element.value);
    this.selectedFieldId = value;
    this.selectedField = null;
    console.log("selectedFieldId: " + this.selectedFieldId);
    document.getElementById("fieldDetails").style.display= "block";
    this.fieldService.getField(this.selectedFieldId).subscribe(field => this.selectedField = field);
    // console.log("Field Size: " + this.selectedField.acres);
    // console.log("Field Desc: " + this.selectedField.fieldDesc);

  }

  refreshSearch(): void {
    this.searchFields(this.recentSearchBoxValue);
    this.getFieldDetails();
  }

  save(): void {
    console.log("selected fieldId: " + this.selectedField.fieldId);
    this.fieldService.updateField(this.selectedField)
      .subscribe(() => this.refreshSearch());
  }

}
