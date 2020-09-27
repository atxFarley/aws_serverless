export interface FieldHistory {

  fieldId: number;
  fieldHistoryId: number;
  fieldHistoryDate: string;
  fieldChangeType: string;
  fieldHistoryOldValue: string;
  fieldHistoryNewValue: string;
  fieldHistoryOldValueGeom: boolean;
  fieldHistoryOldValueGeomId: number;
}
