import {FieldActivityFile} from "./fieldActivityFile";

export interface FieldActivity {

  fieldId: number;
  fieldActivityId; number;
  fieldActivityDate: string;
  fieldActivityType: string;
  fieldActivityDesc: string
  fieldActivityFiles: FieldActivityFile[];
}
