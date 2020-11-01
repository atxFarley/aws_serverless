import {FieldAttribute} from './fieldAttribute';
import {FieldActivity} from './fieldActivity';
import {FieldHistory} from './fieldHistory';

export interface Field {

  fieldId: number;
  fieldName: string;
  fieldDesc: string;
  growerName: string;
  growerId: number;
  ownerName: string;
  ownerId: number;
  acres: number;
  addressStreet: string;
  addressCity: string;
  addressState: string;
  addressZip: string;
  addressCounty: string;
  addressDesc: string;
  fieldAttributes: FieldAttribute[];
  fieldActivities: FieldActivity[];
  fieldHistory: FieldHistory[];

}
