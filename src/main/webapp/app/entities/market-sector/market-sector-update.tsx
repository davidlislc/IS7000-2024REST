import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { SECTOR } from 'app/shared/model/enumerations/sector.model';
import { createEntity, getEntity, reset, updateEntity } from './market-sector.reducer';

export const MarketSectorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marketSectorEntity = useAppSelector(state => state.marketSector.entity);
  const loading = useAppSelector(state => state.marketSector.loading);
  const updating = useAppSelector(state => state.marketSector.updating);
  const updateSuccess = useAppSelector(state => state.marketSector.updateSuccess);
  const sECTORValues = Object.keys(SECTOR);

  const handleClose = () => {
    navigate(`/market-sector${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.change !== undefined && typeof values.change !== 'number') {
      values.change = Number(values.change);
    }

    const entity = {
      ...marketSectorEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          name: 'XLC',
          ...marketSectorEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="is70002024App.marketSector.home.createOrEditLabel" data-cy="MarketSectorCreateUpdateHeading">
            Create or edit a Market Sector
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="market-sector-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Name" id="market-sector-name" name="name" data-cy="name" type="select">
                {sECTORValues.map(sECTOR => (
                  <option value={sECTOR} key={sECTOR}>
                    {sECTOR}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Price"
                id="market-sector-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Change"
                id="market-sector-change"
                name="change"
                data-cy="change"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Marketdate"
                id="market-sector-marketdate"
                name="marketdate"
                data-cy="marketdate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/market-sector" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MarketSectorUpdate;
