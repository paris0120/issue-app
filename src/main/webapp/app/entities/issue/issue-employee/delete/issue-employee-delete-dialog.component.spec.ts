jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IssueEmployeeService } from '../service/issue-employee.service';

import { IssueEmployeeDeleteDialogComponent } from './issue-employee-delete-dialog.component';

describe('IssueEmployee Management Delete Component', () => {
  let comp: IssueEmployeeDeleteDialogComponent;
  let fixture: ComponentFixture<IssueEmployeeDeleteDialogComponent>;
  let service: IssueEmployeeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IssueEmployeeDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(IssueEmployeeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IssueEmployeeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IssueEmployeeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
