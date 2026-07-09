package com.ytk.ytkoj;

import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaEntity;
import com.ytk.ytkoj.domain.problem.infra.jpa.problem.ProblemJpaRepository;
import com.ytk.ytkoj.domain.submission.repository.SubmissionRepository;
import com.ytk.ytkoj.domain.submission.service.SubmissionService;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.global.util.StringCompressor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class YtkojApplicationTests {


	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private ProblemJpaRepository problemJpaRepository;

	@Autowired
	private StringCompressor stringCompressor;

	@Autowired
	private SubmissionRepository submissionRepository;

	private static UserJpaEntity userJpaEntity;
	private static ProblemJpaEntity problemJpaEntity;

	@Test
	void contextLoads() {
	}

//	@BeforeAll
//	public static void before(){
//		user = new User(
//				"t1",
//				"t1",
//				"t1",
//				"t1"
//		);
//
//		problem = new Problem();
//	}

//	@Test
//	void problemTest() throws Exception{
//		userRepository.save(user);
//		problemRepository.save(problem);
//
//		Submission sub = new Submission(user, problem, SubmissionStatus.PENDING, new byte[] {});
//		String rawCode = "import java.util.*;";
//
//		byte[] code = stringCompressor.compress(rawCode);
//		sub.setUserCode(code);
//
//		Submission save = submissionRepository.save(sub);
//
//		String result = stringCompressor.decompress(save.getUserCode());
//
//		System.out.println("압축 결과: " + Arrays.toString(save.getUserCode()));
//		System.out.println("결과: " + result);
//
//
//	}
}
