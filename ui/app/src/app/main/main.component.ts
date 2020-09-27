import {Component, Input, OnInit} from '@angular/core';
import { Field } from '../field';
import {SearchService} from '../search.service';

declare var leafletMap: any;

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {
  @Input()
  searchVal: String;

  searchResults: Field[] = [];


  constructor(private searchService: SearchService) {
  }

  ngOnInit(): void {
    leafletMap.init();
  }


  searchFields(searchboxValue: string): void {
    console.log("searchboxValue: " + searchboxValue);
    this.searchService.searchFields(searchboxValue)
      .subscribe(searchVal => {
        this.searchVal = searchboxValue
        // do something; without data it's just an event
      });
  }


}
