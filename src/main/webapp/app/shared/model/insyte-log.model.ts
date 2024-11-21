import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IInsyteLog {
  id?: number;
  name?: string;
  activity?: string;
  rundate?: dayjs.Dayjs;
  user?: IUser | null;
}

export const defaultValue: Readonly<IInsyteLog> = {};
