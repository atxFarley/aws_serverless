import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';
import {Field} from '../field';
import {SearchService} from '../search.service';
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
  @Input() selectedFieldId: number;


  constructor(private searchService: SearchService, private route: ActivatedRoute,
              private location: Location) {


  }

  ngOnInit(): void {
    leafletMap.init();

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


  searchFields(searchboxValue: string): void {
    console.log("searchboxValue: " + searchboxValue);
    this.searchService.searchFields(searchboxValue)
      .subscribe(searchVal => {
        this.searchVal = searchboxValue
        // do something; without data it's just an event
      });
  }


  getFieldDetails(): void {
    let value = this.selectedFieldId;
    // if (undefined == value) {
    let element: HTMLInputElement = document.getElementById("fieldID") as HTMLInputElement;
    value = parseInt(element.value);
    this.selectedFieldId = value;
    // }
    console.log("selectedFieldId: " + this.selectedFieldId);
  }


}
