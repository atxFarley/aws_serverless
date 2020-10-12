import {FieldAttribute} from "./fieldAttribute";
import {FieldActivity} from "./fieldActivity";
import {FieldHistory} from "./fieldHistory";

export interface Field {

  fieldId: number;
  fieldName: string;
  fieldDesc: string;
  growerName: string;
  ownerName: string;
  acres: number;
  addressStreet: string;
  addressCity: string;
  addressState: string;
  addressZip: string;
  addressCounty: string;
  addressDesc: string;
  attributes: FieldAttribute[];
  activities: FieldActivity[];
  history: FieldHistory[];

}
