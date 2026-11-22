package com.ytk.ytkoj.domain.submission.service.admin;

import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.submission.dto.AdminRequestCommands;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.repository.SubmissionRepository;
import com.ytk.ytkoj.domain.submission.service.CeleryTaskManager;
import com.ytk.ytkoj.global.config.aop.AdminOnly;
import com.ytk.ytkoj.global.exception.InternalServerException;
import com.ytk.ytkoj.global.exception.NoResourceException;
import com.ytk.ytkoj.global.util.StringCompressor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionAdminServiceImpl implements SubmissionAdminService{

    // Submission은 도메인화가 진행되지 않았습니다.
    private final SubmissionRepository submissionRepository;
    private final CeleryTaskManager celeryTaskManager;
    private final StringCompressor stringCompressor;

    @Override
    @Transactional
    @AdminOnly
    public Submission modifyStatus(AdminRequestCommands.ModifyStatusCommand command) {
        String subId = command.submissionId();
        SubmissionStatus modifyStatus = command.status();

        // 엔티티 가져옴
        Submission entity = this.findEntity(subId);

        // 엔티티의 상태 수정
        entity.setStatus(modifyStatus);

        return entity;
    }

    @Override
    @AdminOnly
    public void reGrading(AdminRequestCommands.ReGradingCommand command) {
        String subId = command.submissionId();
        // 엔티티 가져옴
        Submission entity = this.findEntity(subId);


        try{
            // 사용자 소스코드 원본 변환
            String userSource = stringCompressor.decompress(entity.getUserCode());
            ProblemJpaEntity problemJpaEntity = entity.getProblemJpaEntity();
            celeryTaskManager.sendTask(
                    entity.getSubmissionId(),
                    entity.getLang(),
                    userSource,
                    problemJpaEntity.getGrading_data_path(),
                    problemJpaEntity.getTime(),
                    problemJpaEntity.getMemory()
            );
        } catch (Exception e){
            throw new InternalServerException("소스코드 변환 과정 혹은 셀러리 태스크 발송 과정에서 오류 발생: " + e.getMessage());
        }



    }


    private Submission findEntity(String subId){
        return submissionRepository.findBySubmissionId(subId)
                .orElseThrow(() -> new NoResourceException("찾는 오브젝트가 없습니다."));
    }
}
