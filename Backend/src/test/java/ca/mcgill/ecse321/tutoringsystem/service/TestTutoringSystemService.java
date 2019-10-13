package ca.mcgill.ecse321.tutoringsystem.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.mapping.Set;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.portable.ValueOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.tutoringsystem.dao.*;
import ca.mcgill.ecse321.tutoringsystem.model.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTutoringSystemService {

	@Autowired
	TutoringSystemService service;

	@Autowired
	private TutorRepository tutorRepository;
	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private ManagerRepository managerRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private RoomRepository roomRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ApplicationRepository applicationRepository;
	@Autowired
	private InstitutionRepository institutionRepository;
	@Autowired
	private WageRepository wageRepository;
	@Autowired
	private TimeSlotRepository timeslotRepository;

	@After
	public void clearDatabase() {
		requestRepository.deleteAll();
		tutorRepository.deleteAll();
		managerRepository.deleteAll();
		studentRepository.deleteAll();
		timeslotRepository.deleteAll();
		wageRepository.deleteAll();
		institutionRepository.deleteAll();
		applicationRepository.deleteAll();
		reviewRepository.deleteAll();
		notificationRepository.deleteAll();
		roomRepository.deleteAll();
		courseRepository.deleteAll();
	}

	// Tutor class tests

	@Test
	public void testCreateTutor() {
		assertEquals(0, service.getAllTutors().size());
		String name = "Martin";
		String email = "martin@mail.mcgill.ca";
		String password = "password";
		try {
			service.createTutor(name, email, password);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Tutor> allTutors = service.getAllTutors();
		assertEquals(name, allTutors.get(0).getName());
		assertEquals(email, allTutors.get(0).getEmail());
	}

	@Test
	public void testCreateNullTutor() {
		assertEquals(0, service.getAllTutors().size());
		String error = null;
		String name = null;
		String email = null;
		String password = "password";
		try {
			service.createTutor(name, email, password);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Tutor name, email or password cannot be empty!", error);
		assertEquals(0, service.getAllTutors().size());
	}

	@Test
	public void testSetTutorNewName() {
		assertEquals(0, service.getAllTutors().size());
		String name = "Martin";
		String email = "martin@mail.mcgill.ca";
		String password = "password";
		String newName = "George";
		String newEmail = "george@mail.mcgill.ca";
		try {
			Tutor t = service.createTutor(name, email, password);
			t.setName(newName);
			t.setEmail(newEmail);
			tutorRepository.save(t);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Tutor> allTutors = service.getAllTutors();
		assertEquals(newName, allTutors.get(0).getName());
		assertEquals(newEmail, allTutors.get(0).getEmail());
	}

	@Test
	public void testGetTutorName() {
		assertEquals(0, service.getAllTutors().size());
		Tutor t = null;
		String name = "Martin";
		String email = "martin@mail.mcgill.ca";
		String password = "password";
		try {
			t = service.createTutor(name, email, password);
		} catch (IllegalArgumentException e) {
			fail();
		}

		assertEquals(t.getUserId(), service.getTutor(email).getUserId());
	}

	@Test
	public void testGetTutorRequests() {
		assertEquals(0, service.getAllTutors().size());
		String name = "Martin";
		String email = "martin@mail.mcgill.ca";
		String password = "password";
		Time time = Time.valueOf("08:00:01");
		Date date = Date.valueOf("2019-09-22");
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Request request1 = service.createRequest(time, date, tutor, student, course);
		Request request2 = service.createRequest(time, date, tutor, student, course);
		HashSet<Request> requests = new HashSet<Request>();
		requests.add(request1);
		requests.add(request2);
		try {
			Tutor t = service.createTutor(name, email, password);
			t.setRequest(requests);
			tutorRepository.save(t);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Tutor> allTutors = service.getAllTutors();
		assertEquals(true, allTutors.get(0).getRequest().contains(request1));
		assertEquals(true, allTutors.get(0).getRequest().contains(request2));

	}

	// Student class tests

	@Test
	public void testCreateStudent() {
		assertEquals(0, service.getAllStudents().size());
		String name = "Jason";
		String email = "jason@mail.mcgill.ca";
		String password = "password";
		try {
			service.createStudent(name, email, password);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Student> allStudents = service.getAllStudents();
		assertEquals(name, allStudents.get(0).getName());
		assertEquals(email, allStudents.get(0).getEmail());
	}

	@Test
	public void testCreateNullStudent() {
		assertEquals(0, service.getAllStudents().size());
		String error = null;
		String name = null;
		String email = null;
		String password = "password";
		try {
			service.createStudent(name, email, password);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Student name, email or password cannot be empty!", error);
		assertEquals(0, service.getAllStudents().size());
	}

	@Test
	public void testSetStudentNewName() {
		assertEquals(0, service.getAllTutors().size());
		String name = "Jason";
		String email = "jason@mail.mcgill.ca";
		String password = "password";
		String newName = "George";
		String newEmail = "george@mail.mcgill.ca";
		try {
			Student s = service.createStudent(name, email, password);
			s.setName(newName);
			s.setEmail(newEmail);
			studentRepository.save(s);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Student> allStudents = service.getAllStudents();
		assertEquals(newName, allStudents.get(0).getName());
		assertEquals(newEmail, allStudents.get(0).getEmail());
	}

	// Manager class tests

	@Test
	public void testCreateManager() {
		String name = "Marwan";
		String email = "Marwan@mail.mcgill.ca";
		String password = "password";
		try {
			service.createManager(name, email, password);
		} catch (IllegalArgumentException e) {
			fail();
		}

		Manager manager = service.getManager(email);
		assertEquals(name, manager.getName());
		assertEquals(email, manager.getEmail());
	}

	@Test
	public void testCreateNullManager() {
		String error = null;
		String name = null;
		String email = null;
		String password = "password";
		try {
			service.createManager(name, email, password);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Manager name, email or password cannot be empty!", error);
	}

	@Test
	public void testSetManagerNewName() {
		assertEquals(0, service.getAllTutors().size());
		String name = "Marwan";
		String email = "Marwan@mail.mcgill.ca";
		String password = "password";
		String newName = "Daniel";
		String newEmail = "daniel@mail.mcgill.ca";
		try {
			Manager m = service.createManager(name, email, password);
			m.setName(newName);
			m.setEmail(newEmail);
			managerRepository.save(m);
		} catch (IllegalArgumentException e) {
			fail();
		}

		Manager manager = service.getManager(newEmail);
		assertEquals(newName, manager.getName());
		assertEquals(newEmail, manager.getEmail());
	}

	// Request class tests

	@Test
	public void testCreateRequest() {
		assertEquals(0, service.getAllRequests().size());
		Time time = Time.valueOf("08:00:01");
		Date date = Date.valueOf("2019-09-22");
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		try {
			service.createRequest(time, date, tutor, student, course);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Request> allRequests = service.getAllRequests();
		assertEquals(time, allRequests.get(0).getTime());
		assertEquals(date, allRequests.get(0).getDate());
		assertEquals(tutor.getUserId(), allRequests.get(0).getTutor().getUserId());
		assertEquals(student.getUserId(), allRequests.get(0).getStudent().getUserId());
		assertEquals(course.getCourseName(), allRequests.get(0).getCourse().getCourseName());
	}

	@Test
	public void testCreateRequestWithNullTime() {
		assertEquals(0, service.getAllRequests().size());
		String error = null;
		Time time = null;
		Date date = new Date(0);
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		try {
			service.createRequest(time, date, tutor, student, course);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Time cannot be empty!", error);
		assertEquals(0, service.getAllRequests().size());

	}

	@Test
	public void testSetRequestNewStudent() {
		assertEquals(0, service.getAllRequests().size());
		Time time = Time.valueOf("08:00:01");
		Date date = Date.valueOf("2019-09-22");
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name1", "email1", "password1");
		Student newStudent = service.createStudent("name2", "email2", "password2");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		try {
			Request r = service.createRequest(time, date, tutor, student, course);
			r.setStudent(newStudent);
			requestRepository.save(r);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Request> allRequests = service.getAllRequests();
		assertEquals(newStudent.getUserId(), allRequests.get(0).getStudent().getUserId());
	}

	// Course class tests

	@Test
	public void testCreateCourse() {
		assertEquals(0, service.getAllCourses().size());
		String name = "MATH 263";
		Institution institution = service.createInstitution("McGill University", SchoolLevel.University);
		String subjectName = "Mathematics";
		try {
			service.createCourse(name, institution, subjectName);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Course> allCourses = service.getAllCourses();
		assertEquals(name, allCourses.get(0).getCourseName());
		assertEquals(institution.getInstitutionName(), allCourses.get(0).getInstitution().getInstitutionName());
		assertEquals(subjectName, allCourses.get(0).getSubjectName());
	}

	@Test
	public void testCreateCourseWithNullName() {
		assertEquals(0, service.getAllCourses().size());
		String error = null;
		String name = null;
		Institution institution = service.createInstitution("McGill University", SchoolLevel.University);
		String subjectName = "Mathematics";
		try {
			service.createCourse(name, institution, subjectName);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Course name cannot be empty!", error);
		assertEquals(0, service.getAllCourses().size());

	}

	@Test
	public void testSetCourseNewInstitution() {
		assertEquals(0, service.getAllCourses().size());
		String name = "MATH 263";
		Institution institution = service.createInstitution("McGill University", SchoolLevel.University);
		Institution newInstitution = service.createInstitution("CEGEP Bois-de-Boulogne", SchoolLevel.CEGEP);
		String subjectName = "Mathematics";
		try {
			Course c = service.createCourse(name, institution, subjectName);
			c.setInstitution(newInstitution);
			courseRepository.save(c);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Course> allCourses = service.getAllCourses();
		assertEquals(newInstitution.getInstitutionName(), allCourses.get(0).getInstitution().getInstitutionName());
	}

	// Session class tests

	@Test
	public void testCreateAndAcceptSession() {
		assertEquals(0, service.getAllRequests().size());
		Time time = Time.valueOf("08:00:01");
		Date date = Date.valueOf("2019-09-22");
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Request request = service.createRequest(time, date, tutor, student, course);
		Room room = service.createRoom(1, 2);
		try {
			service.acceptRequest(request.getRequestId());
		} catch (IllegalArgumentException e) {
			fail();
		}
		List<Request> allRequests = service.getAllRequests();
		assertEquals(time, allRequests.get(0).getTime());
		assertEquals(date, allRequests.get(0).getDate());
		assertEquals(tutor.getUserId(), allRequests.get(0).getTutor().getUserId());
		assertEquals(student.getUserId(), allRequests.get(0).getStudent().getUserId());
		assertEquals(course.getCourseName(), allRequests.get(0).getCourse().getCourseName());
		assertEquals(room.getRoomNumber(), allRequests.get(0).getRoom().getRoomNumber());
	}

	// Room class tests

	@Test
	public void testCreateRoom() {
		assertEquals(0, service.getAllRooms().size());
		Integer roomNumber = 12;
		Integer capacity = 30;
		try {
			service.createRoom(roomNumber, capacity);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Room> allRooms = service.getAllRooms();
		assertEquals(roomNumber, allRooms.get(0).getRoomNumber());
		assertEquals(capacity, allRooms.get(0).getCapacity());
	}

	@Test
	public void testCreateNullRoom() {
		assertEquals(0, service.getAllRooms().size());
		String error = null;
		Integer roomNumber = null;
		Integer capacity = 30;
		try {
			service.createRoom(roomNumber, capacity);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Room number cannot be empty!", error);
		assertEquals(0, service.getAllRooms().size());
	}

	@Test
	public void testSetRoomNewCapacity() {
		Integer roomNumber = 12;
		Integer capacity = 30;
		Integer newCapacity = 60;
		try {
			Room r = service.createRoom(roomNumber, capacity);
			r.setCapacity(newCapacity);
			roomRepository.save(r);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Room> allRooms = service.getAllRooms();
		assertEquals(newCapacity, allRooms.get(0).getCapacity());

	}

	// Notification class tests

	@Test
	public void testCreateNotification() {
		assertEquals(0, service.getAllNotifications().size());
		Time time = new Time(0);
		Date date = new Date(0);
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student = service.createStudent("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Request request = service.createRequest(time, date, tutor, student, course);
		try {
			service.createNotification(request);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Notification> allNotifications = service.getAllNotifications();
		assertEquals(request.getRequestId(), allNotifications.get(0).getRequest().getRequestId());
	}

	@Test
	public void testCreateNullNotification() {
		assertEquals(0, service.getAllNotifications().size());
		String error = null;
		Request request = null;
		try {
			service.createNotification(request);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Notification ID cannot be null!", error);
		assertEquals(0, service.getAllNotifications().size());
	}

	@Test
	public void testSetNotificationNewRequest() {
		assertEquals(0, service.getAllNotifications().size());
		Time time = new Time(0);
		Date date = new Date(0);
		Tutor tutor = service.createTutor("name", "email", "password");
		Student student1 = service.createStudent("name1", "email1", "password1");
		Student student2 = service.createStudent("name2", "email2", "password2");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Request request = service.createRequest(time, date, tutor, student1, course);
		Request newRequest = service.createRequest(time, date, tutor, student2, course);
		try {
			Notification n = service.createNotification(request);
			n.setRequest(newRequest);
			notificationRepository.save(n);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Notification> allNotifications = service.getAllNotifications();
		assertEquals(newRequest.getRequestId(), allNotifications.get(0).getRequest().getRequestId());
	}

	// Review class tests

	@Test
	public void testCreateReview() {
		// TODO getAllReviews() needs to be implemented to use this test
		Integer rating = 5;
		String comment = "This is a comment.";
		Person from = service.createTutor("name", "email", "password");
		Person to = service.createStudent("name", "email", "password");
		try {
			service.createReview(rating, comment, from, to);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Review> allReviews = service.getAllReviews();
		assertEquals(rating, allReviews.get(0).getRating());
		assertEquals(comment, allReviews.get(0).getComment());
		assertEquals(from.getUserId(), allReviews.get(0).getFrom().getUserId());
		assertEquals(to.getUserId(), allReviews.get(0).getTo().getUserId());
	}

	@Test
	public void testCreateNullReview() {
		// TODO getAllReviews() needs to be implemented to use this test
		String error = null;
		Integer rating = null;
		String comment = null;
		Person from = null;
		Person to = null;
		try {
			service.createReview(rating, comment, from, to);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		List<Review> allReviews = service.getAllReviews();
		assertEquals("Rating cannot be null!", error);
		assertEquals(0, allReviews.size());
	}

	@Test
	public void testSetReviewNewComment() {
		Integer rating = 5;
		String comment = "This is a comment.";
		String newComment = "This is a new comment";
		Person from = service.createTutor("name", "email", "password");
		Person to = service.createStudent("name", "email", "password");
		try {
			Review r = service.createReview(rating, comment, from, to);
			r.setComment(newComment);
			reviewRepository.save(r);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Review> allReviews = service.getAllReviews();
		assertEquals(newComment, allReviews.get(0).getComment());
	}

	// Application class tests

	@Test
	public void testCreateApplication() {
		Boolean isExistingUser = true;
		String name = "Martin";
		String email = "martin@mail.mcgill.ca";
		String course = "ECSE 321";
		try {
			service.createApplication(isExistingUser, name, email, course);
		} catch (IllegalArgumentException e) {
			fail();
		}

		Application application = service.getApplication(email);
		assertEquals(true, application.getIsExistingUser());
		assertEquals(name, application.getName());
		assertEquals(email, application.getEmail());
		assertEquals(course, application.getCourses());
	}

	@Test
	public void testCreateApplicationWithNullName() {
		String error = null;
		Boolean isExistingUser = true;
		String name = null;
		String email = "martin@mail.mcgill.ca";
		String course = "ECSE 321";
		try {
			service.createApplication(isExistingUser, name, email, course);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Application name cannot be empty!", error);
	}

	@Test
	public void testSetApplicationNewEmail() {
		Boolean isExistingUser = true;
		String name = null;
		String email = "martin@mail.mcgill.ca";
		String newEmail = "george@mail.mcgill.ca";
		String course = "ECSE 321";
		try {
			Application a = service.createApplication(isExistingUser, name, email, course);
			a.setEmail(newEmail);
			applicationRepository.save(a);
		} catch (IllegalArgumentException e) {
			fail();
		}

		Application application = service.getApplication(newEmail);
		assertEquals(newEmail, application.getEmail());
	}

	// Institution class tests

	@Test
	public void testCreateInstitution() {
		assertEquals(0, service.getAllInstitutions().size());
		String institutionName = "McGill University";
		SchoolLevel institutionLevel = SchoolLevel.University;
		try {
			service.createInstitution(institutionName, institutionLevel);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Institution> allInstitutions = service.getAllInstitutions();
		assertEquals(institutionName, allInstitutions.get(0).getInstitutionName());
		assertEquals(institutionLevel, allInstitutions.get(0).getInstitutionLevel());
	}

	@Test
	public void testCreateNullInstitution() {
		assertEquals(0, service.getAllInstitutions().size());
		String error = null;
		String institutionName = null;
		SchoolLevel institutionLevel = SchoolLevel.University;
		try {
			service.createInstitution(institutionName, institutionLevel);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("Institution name cannot be null!", error);
		assertEquals(0, service.getAllInstitutions().size());

	}

	@Test
	public void testSetInstitutionNewName() {
		assertEquals(0, service.getAllInstitutions().size());
		boolean pass = false;
		String institutionName = "McGill University";
		String newInstitutionName = "Concordia University";
		SchoolLevel institutionLevel = SchoolLevel.University;
		try {
			Institution i = service.createInstitution(institutionName, institutionLevel);
			i.setInstitutionName(newInstitutionName);
			institutionRepository.save(i);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Institution> allInstitutions = service.getAllInstitutions();
		for (Institution i : allInstitutions) {
			if (i.getInstitutionName().equals(newInstitutionName))
				pass = true;
		}
		assertEquals(true, pass);
	}

	// Wage class tests

	@Test
	public void testCreateWage() {
		assertEquals(0, service.getAllWages().size());
		Tutor tutor = service.createTutor("name", "email", "password");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Integer wage = 20;
		try {
			service.createWage(tutor, course, wage);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Wage> allWages = service.getAllWages();
		assertEquals(tutor.getUserId(), allWages.get(0).getTutor().getUserId());
		assertEquals(course.getCourseName(), allWages.get(0).getCourse().getCourseName());
		assertEquals(wage, allWages.get(0).getWage());
	}

	@Test
	public void testCreateNullWage() {
		assertEquals(0, service.getAllWages().size());
		String error = null;
		Tutor tutor = null;
		Course course = null;
		Integer wage = 20;
		try {
			service.createWage(tutor, course, wage);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("A tutor needs to be specified!", error);
		assertEquals(0, service.getAllWages().size());
	}

	@Test
	public void testSetWageNewTutor() {
		assertEquals(0, service.getAllWages().size());
		Tutor tutor = service.createTutor("name1", "email1", "password1");
		Tutor newTutor = service.createTutor("name2", "email2", "password2");
		Course course = service.createCourse("test",
				service.createInstitution("institutionName", SchoolLevel.University), "subject");
		Integer wage = 20;
		try {
			Wage w = service.createWage(tutor, course, wage);
			w.setTutor(newTutor);
			wageRepository.save(w);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<Wage> allWages = service.getAllWages();
		assertEquals(newTutor.getUserId(), allWages.get(0).getTutor().getUserId());
	}

	// TimeSlot class tests

	@Test
	public void testCreateTimeSlot() {
		Time time = new Time(0);
		Date date = new Date(0);
		Tutor tutor = service.createTutor("name", "email", "password");
		try {
			service.createTimeSlot(tutor, date, time);
		} catch (IllegalArgumentException e) {
			fail();
		}

		List<TimeSlot> t = service.getTimeSlot(date, time);
		assertEquals(tutor.getUserId(), t.get(0).getTutor().getUserId());
	}

	@Test
	public void testCreateNullTimeSlot() {
		String error = null;
		Time time = new Time(0);
		Date date = new Date(0);
		Tutor tutor = null;
		try {
			service.createTimeSlot(tutor, date, time);
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		assertEquals("A tutor needs to be specified!", error);
		assertEquals(0, service.getAllWages().size());
	}

	@Test
	public void setTimeSlotNewTutor() {
		Time time = new Time(0);
		Date date = new Date(0);
		Tutor tutor = service.createTutor("name1", "email1", "password1");
		Tutor newTutor = service.createTutor("name2", "email2", "password2");
		try {
			TimeSlot t = service.createTimeSlot(tutor, date, time);
			t.setTutor(newTutor);
			timeslotRepository.save(t);
		} catch (IllegalArgumentException e) {
			fail();
		}
	}
}