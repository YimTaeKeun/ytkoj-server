package com.ytk.ytkoj;

import com.ytk.ytkoj.domain.problem.entity.Problem;
import com.ytk.ytkoj.domain.problem.repository.ProblemRepository;
import com.ytk.ytkoj.domain.problem.service.ProblemService;
import com.ytk.ytkoj.domain.submission.entity.Submission;
import com.ytk.ytkoj.domain.submission.entity.SubmissionStatus;
import com.ytk.ytkoj.domain.submission.repository.SubmissionRepository;
import com.ytk.ytkoj.domain.submission.service.SubmissionService;
import com.ytk.ytkoj.domain.usr.entity.User;
import com.ytk.ytkoj.domain.usr.repository.UserRepository;
import com.ytk.ytkoj.global.util.StringCompressor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class YtkojApplicationTests {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private SubmissionService submissionService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProblemRepository problemRepository;

	@Autowired
	private StringCompressor stringCompressor;

	@Autowired
	private SubmissionRepository submissionRepository;

	private static User user;
	private static Problem problem;

	@Test
	void contextLoads() {
	}

	@BeforeAll
	public static void before(){
		user = new User(
				"t1",
				"t1",
				"t1",
				"t1"
		);

		problem = new Problem();
	}

	@Test
	void problemTest() throws Exception{
		userRepository.save(user);
		problemRepository.save(problem);

		Submission sub = new Submission(user, problem, SubmissionStatus.PENDING);
		String rawCode = "import java.util.*;";

		byte[] code = stringCompressor.compress(rawCode);
		sub.setUserCode(code);

		Submission save = submissionRepository.save(sub);

		String result = stringCompressor.decompress(save.getUserCode());

		System.out.println("압축 결과: " + Arrays.toString(save.getUserCode()));
		System.out.println("결과: " + result);


	}
}
