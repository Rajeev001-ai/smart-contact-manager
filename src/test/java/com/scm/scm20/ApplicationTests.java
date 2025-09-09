package com.scm.scm20;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.scm20.userService.EmailService;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService emailService;

	@Test
	void sendEmailTest(){

		emailService.sendEmail(
			"rajeevpatel2025@gmail.com",
			"just testing email service",
			"this is scm project working on email service"
		);
	}

}
