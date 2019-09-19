// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { UbicacionComponentsPage, UbicacionDeleteDialog, UbicacionUpdatePage } from './ubicacion.page-object';

const expect = chai.expect;

describe('Ubicacion e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ubicacionUpdatePage: UbicacionUpdatePage;
  let ubicacionComponentsPage: UbicacionComponentsPage;
  let ubicacionDeleteDialog: UbicacionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Ubicacions', async () => {
    await navBarPage.goToEntity('ubicacion');
    ubicacionComponentsPage = new UbicacionComponentsPage();
    await browser.wait(ec.visibilityOf(ubicacionComponentsPage.title), 5000);
    expect(await ubicacionComponentsPage.getTitle()).to.eq('hjisterapp1App.ubicacion.home.title');
  });

  it('should load create Ubicacion page', async () => {
    await ubicacionComponentsPage.clickOnCreateButton();
    ubicacionUpdatePage = new UbicacionUpdatePage();
    expect(await ubicacionUpdatePage.getPageTitle()).to.eq('hjisterapp1App.ubicacion.home.createOrEditLabel');
    await ubicacionUpdatePage.cancel();
  });

  it('should create and save Ubicacions', async () => {
    const nbButtonsBeforeCreate = await ubicacionComponentsPage.countDeleteButtons();

    await ubicacionComponentsPage.clickOnCreateButton();
    await promise.all([
      ubicacionUpdatePage.setCalleInput('calle'),
      ubicacionUpdatePage.setCodigoPostalInput('codigoPostal'),
      ubicacionUpdatePage.setCiudadInput('ciudad'),
      ubicacionUpdatePage.setStateProvinceInput('stateProvince'),
      ubicacionUpdatePage.provinciaSelectLastOption(),
      ubicacionUpdatePage.nombreUbicacionSelectLastOption()
    ]);
    expect(await ubicacionUpdatePage.getCalleInput()).to.eq('calle', 'Expected Calle value to be equals to calle');
    expect(await ubicacionUpdatePage.getCodigoPostalInput()).to.eq(
      'codigoPostal',
      'Expected CodigoPostal value to be equals to codigoPostal'
    );
    expect(await ubicacionUpdatePage.getCiudadInput()).to.eq('ciudad', 'Expected Ciudad value to be equals to ciudad');
    expect(await ubicacionUpdatePage.getStateProvinceInput()).to.eq(
      'stateProvince',
      'Expected StateProvince value to be equals to stateProvince'
    );
    await ubicacionUpdatePage.save();
    expect(await ubicacionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ubicacionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Ubicacion', async () => {
    const nbButtonsBeforeDelete = await ubicacionComponentsPage.countDeleteButtons();
    await ubicacionComponentsPage.clickOnLastDeleteButton();

    ubicacionDeleteDialog = new UbicacionDeleteDialog();
    expect(await ubicacionDeleteDialog.getDialogTitle()).to.eq('hjisterapp1App.ubicacion.delete.question');
    await ubicacionDeleteDialog.clickOnConfirmButton();

    expect(await ubicacionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
