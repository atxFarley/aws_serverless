import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges
} from '@angular/core';


import {Field} from '../field';
import {FieldUser} from '../fieldUser';
import {FieldAttribute} from '../fieldAttribute';
import {FieldActivity} from '../fieldActivity';
import {FieldActivityType} from '../fieldActivityType';
import {FieldActivityFileType} from '../fieldActivityFileType';
import {FieldActivityFile} from '../fieldActivityFile';
import {SearchService} from '../search.service';
import {FieldService} from '../field.service';
import {FielduserService} from '../fielduser.service';
import {FieldattributeService} from '../fieldattribute.service';
import {FieldactivityService} from '../fieldactivity.service';
import {FieldactivityfileService} from '../fieldactivityfile.service';
import {FieldactivitytypeService} from '../fieldactivitytype.service';
import {FieldactivityfiletypeService} from '../fieldactivityfiletype.service';
import {FileuploadService} from '../fileupload.service';
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
  @Input() searchVal: string;

  searchResults: Field[] = [];
  selectedField: Field;
  fieldUsers: FieldUser[];
  fieldAttributes: FieldAttribute[];
  activityTypes: FieldActivityType[];
  activityFileTypes: FieldActivityFileType[];
  recentSearchBoxValue: string;
  @Input() selectedFieldId: number;
  uploadFile: File = null;
  uploadedFieldActivityFile: FieldActivityFile;
  selectedFieldActivityId: string;
  newFieldActivity: FieldActivity;
  newFieldAttribute: FieldAttribute;
  newFieldAttributeValues: string[];

  constructor(private searchService: SearchService,
              private route: ActivatedRoute,
              private location: Location,
              private fieldService: FieldService,
              private fieldUserService: FielduserService,
              private fieldAttributeService: FieldattributeService,
              private fieldactivityService: FieldactivityService,
              private fieldactivityfileService: FieldactivityfileService,
              private fieldactivitytypeService: FieldactivitytypeService,
              private fieldactivityfiletypeService: FieldactivityfiletypeService,
              private fileuploadService: FileuploadService) {


  }

  ngOnInit(): void {
    console.log('main ngOnInit()');
    leafletMap.init(environment.apiURL, environment.mapboxUrl);
    this.getFieldUsers();
    this.getFieldAttributes();
    this.getFieldActivityTypes();
    this.getFieldActivityFileTypes();
    this.newFieldActivity = {} as FieldActivity;
    this.newFieldAttribute = {} as FieldAttribute;
    this.newFieldAttribute.attributeValues = {} as string[];

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

  getFieldAttributes(): void {
    this.fieldAttributeService.getFieldAttributes().subscribe(fieldAttributes => this.fieldAttributes = fieldAttributes);
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
    console.log('searchboxValue: ' + searchboxValue);
    this.recentSearchBoxValue = searchboxValue;
    this.searchService.searchFields(searchboxValue)
      .subscribe(searchVal => {
        this.searchVal = searchboxValue;
        // do something; without data it's just an event
      });
  }


  getFieldDetails(): void {
    let value = this.selectedFieldId;
    const element: HTMLInputElement = document.getElementById('fieldID') as HTMLInputElement;
    value = parseInt(element.value, 10);
    this.selectedFieldId = value;
    this.selectedField = null;
    console.log('selectedFieldId: ' + this.selectedFieldId);
    document.getElementById('fieldDetails').style.display = 'block';
    this.fieldService.getField(this.selectedFieldId).subscribe(field => this.selectedField = field);
    // console.log('Field Size: ' + this.selectedField.acres);
    // console.log('Field Desc: ' + this.selectedField.fieldDesc);

  }

  removeFieldAttribute(fieldAttributeName, fieldAttributeValue): void {
    console.log('field Attribute to remove: ' + fieldAttributeName + ': ' + fieldAttributeValue);
    for (let i = 0; i < this.selectedField.fieldAttributes.length; i++) {
      if (this.selectedField.fieldAttributes[i].attributeName === fieldAttributeName
        && this.selectedField.fieldAttributes[i].attributeValues[0] === fieldAttributeValue) {
        this.selectedField.fieldAttributes.splice(i, 1);
        i--;
      }
    }
    console.log('select field attributes: ' + this.selectedField.fieldAttributes);
  }

  removeFieldActivity(fieldActivityId): void {
    console.log('removeFieldActivity: fieldActivityId: ' + fieldActivityId);
    const deleteFieldActivity: FieldActivity = {} as FieldActivity;
    deleteFieldActivity.fieldActivityId = fieldActivityId;
    deleteFieldActivity.fieldId = this.selectedFieldId;
    const deleteActivityUrl = this.fieldactivityService.deleteFieldActivity(this.selectedFieldId.toString(), deleteFieldActivity)
      .pipe(
        concatMap((data: any) => {
            this.getFieldDetails();
            return of();
          }
        )
      );
    deleteActivityUrl.subscribe(() => console.log('completed entire delete activity sequence'));
  }

  removeFieldActivityFile(fieldActivityId, fieldActivityFileId, fieldActivityFileLocation): void {
    console.log('removeFieldActivityFile(fieildActivityFileId: ' + fieldActivityFileId +
      ', fieldActivityFileLocation: ' + fieldActivityFileLocation + ')');
    const deleteFieldActivityFile: FieldActivityFile = {} as FieldActivityFile;
    deleteFieldActivityFile.fieldActivityFileId = fieldActivityFileId;
    deleteFieldActivityFile.fieldActivityFileLocation = fieldActivityFileLocation;
    const deleteFileURL = this.fieldactivityfileService.deleteBucketFile(this.selectedFieldId.toString(), fieldActivityId.toString(),
      deleteFieldActivityFile)
      .pipe(
        concatMap(fileUpload => this.fieldactivityfileService.deleteFieldActivityFile(this.selectedFieldId.toString(),
          fieldActivityId.toString(), deleteFieldActivityFile)
          .pipe(
            concatMap((data: any) => {
                this.getFieldDetails();
                return of();
              }
            )
          )
        )
      );
    deleteFileURL.subscribe(() => console.log('completed entire delete file sequence'));
  }

  refreshSearch(): void {
    this.newFieldActivity = {} as FieldActivity;
    this.newFieldAttribute = {} as FieldAttribute;
    this.newFieldAttribute.attributeValues = {} as string[];
    this.searchFields(this.recentSearchBoxValue);
    this.getFieldDetails();
  }

  save(): void {
    console.log('selected fieldId: ' + this.selectedField.fieldId);
    this.fieldService.updateField(this.selectedField)
      .subscribe(() => this.refreshSearch());
  }

  fileUpload(): any {
    console.log('fileUpload()');
    const fileUploadURL = this.fileuploadService.getPresignedURL(this.uploadFile, this.selectedFieldId.toString(),
      this.selectedFieldActivityId)
      .pipe(
        concatMap(fileUpload => this.fileuploadService.uploadFile(fileUpload, this.uploadFile, this.selectedFieldId.toString(),
          this.selectedFieldActivityId)
          .pipe(
            concatMap(fieldActivityFile => this.fileuploadService.addFieldActivityFile(this.selectedFieldId.toString(),
              this.selectedFieldActivityId, fieldActivityFile)
              .pipe(
                concatMap((data: any) => {
                    this.getFieldDetails();
                    return of();
                  }
                )
              )
            )
          )
        )
      );
    fileUploadURL.subscribe(() => console.log('completed entire sequence'));
  }


  fileChange(event, selectedFieldActivityId): void {
    this.uploadFile = event.target.files[0];
    this.selectedFieldActivityId = selectedFieldActivityId;
  }

  populateNewFieldAttributeValues(): void {
    console.log('populateNewFieldAttributeValues: ');
    if (this.newFieldAttribute.attributeName !== '-') {
      this.newFieldAttributeValues = this.fieldAttributes
        .find(name => name.attributeName === this.newFieldAttribute.attributeName).attributeValues;
    } else {
      this.newFieldAttributeValues = {} as string[];
    }
  }

  addFieldAttribute(): void {
    console.log('addFieldAttribute: ' + this.newFieldAttribute.attributeName + ': ' + this.newFieldAttribute.attributeValues[0]);
    if ((this.newFieldAttribute.attributeName !== '' && this.newFieldAttribute.attributeName !== undefined
      && this.newFieldAttribute.attributeName !== 'undefined') && (this.newFieldAttribute.attributeValues[0] !== ''
      && this.newFieldAttribute.attributeValues[0] !== undefined && this.newFieldAttribute.attributeValues[0] !== 'undefined')) {
      this.newFieldAttribute.fieldId = this.selectedFieldId;
      console.log(this.newFieldAttribute.attributeValues[0].trim());
      console.log(this.newFieldAttribute.attributeValues);
      const addFieldAttribute: FieldAttribute = {} as FieldAttribute;
      addFieldAttribute.attributeValues = [this.newFieldAttribute.attributeValues[0].trim()];
      addFieldAttribute.fieldId = this.selectedFieldId;
      addFieldAttribute.attributeName = this.newFieldAttribute.attributeName.trim();
      console.log('addFieldAttribute: ' + addFieldAttribute);
      this.selectedField.fieldAttributes.push(addFieldAttribute);
    }
    console.log('select field attributes: ' + this.selectedField.fieldAttributes);
  }

  addFieldActivity(): void {
    console.log('new field activity: ' + this.newFieldActivity);
    this.newFieldActivity.fieldId = this.selectedField.fieldId;
    console.log('new filed activity field id: ' + this.newFieldActivity.fieldId);
    this.fieldactivityService.addFieldActivity(this.newFieldActivity)
      .subscribe(() => this.refreshSearch());
  }

}
