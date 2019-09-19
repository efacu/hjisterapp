// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { ProvinciaComponentsPage, ProvinciaDeleteDialog, ProvinciaUpdatePage } from './provincia.page-object';

const expect = chai.expect;

describe('Provincia e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let provinciaUpdatePage: ProvinciaUpdatePage;
  let provinciaComponentsPage: ProvinciaComponentsPage;
  let provinciaDeleteDialog: ProvinciaDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Provincias', async () => {
    await navBarPage.goToEntity('provincia');
    provinciaComponentsPage = new ProvinciaComponentsPage();
    await browser.wait(ec.visibilityOf(provinciaComponentsPage.title), 5000);
    expect(await provinciaComponentsPage.getTitle()).to.eq('hjisterapp1App.provincia.home.title');
  });

  it('should load create Provincia page', async () => {
    await provinciaComponentsPage.clickOnCreateButton();
    provinciaUpdatePage = new ProvinciaUpdatePage();
    expect(await provinciaUpdatePage.getPageTitle()).to.eq('hjisterapp1App.provincia.home.createOrEditLabel');
    await provinciaUpdatePage.cancel();
  });

  it('should create and save Provincias', async () => {
    const nbButtonsBeforeCreate = await provinciaComponentsPage.countDeleteButtons();

    await provinciaComponentsPage.clickOnCreateButton();
    await promise.all([provinciaUpdatePage.setNombreProvinciaInput('nombreProvincia'), provinciaUpdatePage.countrySelectLastOption()]);
    expect(await provinciaUpdatePage.getNombreProvinciaInput()).to.eq(
      'nombreProvincia',
      'Expected NombreProvincia value to be equals to nombreProvincia'
    );
    await provinciaUpdatePage.save();
    expect(await provinciaUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await provinciaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Provincia', async () => {
    const nbButtonsBeforeDelete = await provinciaComponentsPage.countDeleteButtons();
    await provinciaComponentsPage.clickOnLastDeleteButton();

    provinciaDeleteDialog = new ProvinciaDeleteDialog();
    expect(await provinciaDeleteDialog.getDialogTitle()).to.eq('hjisterapp1App.provincia.delete.question');
    await provinciaDeleteDialog.clickOnConfirmButton();

    expect(await provinciaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
