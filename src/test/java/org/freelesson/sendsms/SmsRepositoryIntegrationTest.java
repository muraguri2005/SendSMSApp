package org.freelesson.sendsms;

import org.freelesson.sendsms.repository.SmsRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SmsRepositoryIntegrationTest {
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private SmsRepository smsRepository;
}
