import { element, by, ElementFinder } from 'protractor';

export class UbicacionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-ubicacion div table .btn-danger'));
  title = element.all(by.css('jhi-ubicacion div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class UbicacionUpdatePage {
  pageTitle = element(by.id('jhi-ubicacion-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  calleInput = element(by.id('field_calle'));
  codigoPostalInput = element(by.id('field_codigoPostal'));
  ciudadInput = element(by.id('field_ciudad'));
  stateProvinceInput = element(by.id('field_stateProvince'));
  provinciaSelect = element(by.id('field_provincia'));
  nombreUbicacionSelect = element(by.id('field_nombreUbicacion'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCalleInput(calle) {
    await this.calleInput.sendKeys(calle);
  }

  async getCalleInput() {
    return await this.calleInput.getAttribute('value');
  }

  async setCodigoPostalInput(codigoPostal) {
    await this.codigoPostalInput.sendKeys(codigoPostal);
  }

  async getCodigoPostalInput() {
    return await this.codigoPostalInput.getAttribute('value');
  }

  async setCiudadInput(ciudad) {
    await this.ciudadInput.sendKeys(ciudad);
  }

  async getCiudadInput() {
    return await this.ciudadInput.getAttribute('value');
  }

  async setStateProvinceInput(stateProvince) {
    await this.stateProvinceInput.sendKeys(stateProvince);
  }

  async getStateProvinceInput() {
    return await this.stateProvinceInput.getAttribute('value');
  }

  async provinciaSelectLastOption(timeout?: number) {
    await this.provinciaSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async provinciaSelectOption(option) {
    await this.provinciaSelect.sendKeys(option);
  }

  getProvinciaSelect(): ElementFinder {
    return this.provinciaSelect;
  }

  async getProvinciaSelectedOption() {
    return await this.provinciaSelect.element(by.css('option:checked')).getText();
  }

  async nombreUbicacionSelectLastOption(timeout?: number) {
    await this.nombreUbicacionSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async nombreUbicacionSelectOption(option) {
    await this.nombreUbicacionSelect.sendKeys(option);
  }

  getNombreUbicacionSelect(): ElementFinder {
    return this.nombreUbicacionSelect;
  }

  async getNombreUbicacionSelectedOption() {
    return await this.nombreUbicacionSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class UbicacionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-ubicacion-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-ubicacion'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
