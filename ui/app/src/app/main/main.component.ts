import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';


import {Field} from '../field';
import {FieldUser} from "../fieldUser";
import {FieldActivityType} from "../fieldActivityType";
import {FieldActivityFileType} from "../fieldActivityFileType";
import {FieldActivityFile} from "../fieldActivityFile";
import {SearchService} from '../search.service';
import {FieldService} from "../field.service";
import {FielduserService} from "../fielduser.service";
import {FieldactivitytypeService} from "../fieldactivitytype.service";
import {FieldactivityfiletypeService} from "../fieldactivityfiletype.service";
import {FileuploadService} from "../fileupload.service";
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {Observable, of} from 'rxjs';
import {catchError, map, tap, concatMap} from 'rxjs/operators';
import {environment} from '../../environments/environment';

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
  activityTypes: FieldActivityType[];
  activityFileTypes: FieldActivityFileType[];
  recentSearchBoxValue: string;
  @Input() selectedFieldId: number;
  uploadFile: File = null;
  uploadedFieldActivityFile: FieldActivityFile;
  selectedFieldActivityId: string;

  constructor(private searchService: SearchService, private route: ActivatedRoute,
              private location: Location, private fieldService: FieldService, private fieldUserService: FielduserService,
              private fieldactivitytypeService: FieldactivitytypeService, private fieldactivityfiletypeService: FieldactivityfiletypeService,
              private fileuploadService: FileuploadService) {


  }

  ngOnInit(): void {
    leafletMap.init(environment.apiURL, environment.mapboxUrl);
    this.getFieldUsers();
    this.getFieldActivityTypes();
    this.getFieldActivityFileTypes();

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

  getFieldActivityTypes(): void {
    this.fieldactivitytypeService.getFieldActivityTypes()
      .subscribe(activityTypes => this.activityTypes = activityTypes);
  }

  getFieldActivityFileTypes(): void {
    this.fieldactivityfiletypeService.getFieldActivityFileTypes()
      .subscribe(activityFileTypes => this.activityFileTypes = activityFileTypes);
  }

  searchFields(searchboxValue: string): void {
    console.log("searchboxValue: " + searchboxValue);
    this.recentSearchBoxValue = searchboxValue;
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
    document.getElementById("fieldDetails").style.display = "block";
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

  fileUpload() {
    console.log("fileUpload()");
    let fileUploadURL = this.fileuploadService.getPresignedURL(this.uploadFile, this.selectedFieldId.toString(), this.selectedFieldActivityId).pipe(
      concatMap(fileUpload => this.fileuploadService.uploadFile(fileUpload, this.uploadFile, this.selectedFieldId.toString(), this.selectedFieldActivityId).pipe(
        concatMap(fieldActivityFile => this.fileuploadService.addFieldActivityFile(this.selectedFieldId.toString(), this.selectedFieldActivityId, fieldActivityFile)
          .pipe(
            concatMap((data: any) => {
                this.getFieldDetails()
                return of()
              }
            )
          )
        )
        )
      )
    );
    fileUploadURL.subscribe(() => console.log("completed entire sequence"));
  }


  fileChange(event, selectedFieldActivityId) {
    this.uploadFile = event.target.files[0];
    this.selectedFieldActivityId = selectedFieldActivityId;
  }

}
