import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IGiftcard {
  id?: number;
  name?: string;
  giftcardamount?: number;
  addDate?: dayjs.Dayjs;
  user?: IUser | null;
}

export const defaultValue: Readonly<IGiftcard> = {};
