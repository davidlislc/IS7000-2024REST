import { IUser } from 'app/shared/model/user.model';

export interface IWallet {
  id?: number;
  name?: string;
  credit?: number;
  giftcard?: number;
  user?: IUser | null;
}

export const defaultValue: Readonly<IWallet> = {};
