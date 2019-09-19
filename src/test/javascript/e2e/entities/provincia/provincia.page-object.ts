import { element, by, ElementFinder } from 'protractor';

export class ProvinciaComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-provincia div table .btn-danger'));
  title = element.all(by.css('jhi-provincia div h2#page-heading span')).first();

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

export class ProvinciaUpdatePage {
  pageTitle = element(by.id('jhi-provincia-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  nombreProvinciaInput = element(by.id('field_nombreProvincia'));
  countrySelect = element(by.id('field_country'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNombreProvinciaInput(nombreProvincia) {
    await this.nombreProvinciaInput.sendKeys(nombreProvincia);
  }

  async getNombreProvinciaInput() {
    return await this.nombreProvinciaInput.getAttribute('value');
  }

  async countrySelectLastOption(timeout?: number) {
    await this.countrySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async countrySelectOption(option) {
    await this.countrySelect.sendKeys(option);
  }

  getCountrySelect(): ElementFinder {
    return this.countrySelect;
  }

  async getCountrySelectedOption() {
    return await this.countrySelect.element(by.css('option:checked')).getText();
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

export class ProvinciaDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-provincia-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-provincia'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
