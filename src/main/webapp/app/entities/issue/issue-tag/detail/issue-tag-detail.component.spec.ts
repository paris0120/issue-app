import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IssueTagDetailComponent } from './issue-tag-detail.component';

describe('IssueTag Management Detail Component', () => {
  let comp: IssueTagDetailComponent;
  let fixture: ComponentFixture<IssueTagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IssueTagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ issueTag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IssueTagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IssueTagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load issueTag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.issueTag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
