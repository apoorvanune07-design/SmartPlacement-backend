package com.smartplacement.config;

import com.smartplacement.entity.*;
import com.smartplacement.enums.*;
import com.smartplacement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyRepository companyRepository;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewScheduleRepository interviewScheduleRepository;
    private final AnnouncementRepository announcementRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            return; // Data already seeded
        }

        log.info("Initializing Seed Data for Smart Student Placement System...");

        // 1. Roles
        Role studentRole = roleRepository.save(new Role(RoleType.ROLE_STUDENT));
        Role officerRole = roleRepository.save(new Role(RoleType.ROLE_OFFICER));
        Role adminRole = roleRepository.save(new Role(RoleType.ROLE_ADMIN));

        // 2. Placement Officer Account
        User officerUser = userRepository.save(User.builder()
                .email("officer@college.edu")
                .password(passwordEncoder.encode("officer123"))
                .fullName("Prof. Rajesh Sharma (TPO Head)")
                .phone("+91 9876543210")
                .enabled(true)
                .roles(new HashSet<>(Set.of(officerRole)))
                .build());

        // 3. Admin Account
        userRepository.save(User.builder()
                .email("admin@college.edu")
                .password(passwordEncoder.encode("admin123"))
                .fullName("System Administrator")
                .phone("+91 9876543211")
                .enabled(true)
                .roles(new HashSet<>(Set.of(adminRole, officerRole)))
                .build());

        // 4. Student Accounts & Profiles
        // Student 1: Rahul Sharma (Eligible for most jobs, CGPA 8.7)
        User rahulUser = userRepository.save(User.builder()
                .email("rahul@student.edu")
                .password(passwordEncoder.encode("student123"))
                .fullName("Rahul Sharma")
                .phone("+91 9123456780")
                .enabled(true)
                .roles(new HashSet<>(Set.of(studentRole)))
                .build());

        StudentProfile rahulProfile = studentProfileRepository.save(StudentProfile.builder()
                .user(rahulUser)
                .rollNumber("21CS042")
                .branch("CSE")
                .degree("B.Tech")
                .passingYear(2025)
                .cgpa(8.75)
                .tenthPercentage(92.4)
                .twelfthPercentage(89.0)
                .activeBacklogs(0)
                .totalBacklogs(0)
                .skills("Java, Spring Boot, React, MySQL, Docker, REST APIs, Git")
                .githubUrl("https://github.com/rahul-sharma-dev")
                .linkedinUrl("https://linkedin.com/in/rahul-sharma-dev")
                .portfolioUrl("https://rahulsharma.dev")
                .bio("Passionate full-stack developer specializing in Spring Boot and React ecosystems. Built high-scale web apps and microservices.")
                .isPlaced(false)
                .build());

        // Student 2: Priya Patel (High CGPA 9.3, AI/ML enthusiast)
        User priyaUser = userRepository.save(User.builder()
                .email("priya@student.edu")
                .password(passwordEncoder.encode("student123"))
                .fullName("Priya Patel")
                .phone("+91 9123456781")
                .enabled(true)
                .roles(new HashSet<>(Set.of(studentRole)))
                .build());

        StudentProfile priyaProfile = studentProfileRepository.save(StudentProfile.builder()
                .user(priyaUser)
                .rollNumber("21IT019")
                .branch("IT")
                .degree("B.Tech")
                .passingYear(2025)
                .cgpa(9.30)
                .tenthPercentage(95.0)
                .twelfthPercentage(94.2)
                .activeBacklogs(0)
                .totalBacklogs(0)
                .skills("Python, Machine Learning, TensorFlow, AWS, SQL, React")
                .githubUrl("https://github.com/priyapatel")
                .linkedinUrl("https://linkedin.com/in/priya-patel-ai")
                .bio("Aspiring Machine Learning and Cloud Engineer with research publications in NLP.")
                .isPlaced(false)
                .build());

        // Student 3: Arjun Verma (ECE, CGPA 6.8, 1 Backlog)
        User arjunUser = userRepository.save(User.builder()
                .email("arjun@student.edu")
                .password(passwordEncoder.encode("student123"))
                .fullName("Arjun Verma")
                .phone("+91 9123456782")
                .enabled(true)
                .roles(new HashSet<>(Set.of(studentRole)))
                .build());

        StudentProfile arjunProfile = studentProfileRepository.save(StudentProfile.builder()
                .user(arjunUser)
                .rollNumber("21EC055")
                .branch("ECE")
                .degree("B.Tech")
                .passingYear(2025)
                .cgpa(6.80)
                .tenthPercentage(78.0)
                .twelfthPercentage(74.5)
                .activeBacklogs(1)
                .totalBacklogs(1)
                .skills("Embedded C, IoT, Microcontrollers, Python, Verilog")
                .githubUrl("https://github.com/arjunv")
                .bio("Hardware and Embedded firmware developer with hands-on microcontroller projects.")
                .isPlaced(false)
                .build());

        // Student 4: Sneha Kulkarni (Already Placed at Microsoft, 18 LPA)
        User snehaUser = userRepository.save(User.builder()
                .email("sneha@student.edu")
                .password(passwordEncoder.encode("student123"))
                .fullName("Sneha Kulkarni")
                .phone("+91 9123456783")
                .enabled(true)
                .roles(new HashSet<>(Set.of(studentRole)))
                .build());

        studentProfileRepository.save(StudentProfile.builder()
                .user(snehaUser)
                .rollNumber("21CS088")
                .branch("CSE")
                .degree("B.Tech")
                .passingYear(2025)
                .cgpa(9.10)
                .tenthPercentage(96.0)
                .twelfthPercentage(93.0)
                .activeBacklogs(0)
                .totalBacklogs(0)
                .skills("Go, Java, Distributed Systems, Kubernetes, Cloud")
                .isPlaced(true)
                .placedCompany("Microsoft")
                .packageLpa(18.5)
                .build());

        // 5. Companies
        Company google = companyRepository.save(Company.builder()
                .name("Google")
                .industry("Technology & Cloud")
                .website("https://careers.google.com")
                .description("Google is a global technology leader focused on improving the ways people connect with information.")
                .contactPerson("Sarah Jenkins")
                .contactEmail("university-recruitment@google.com")
                .contactPhone("+1 650-253-0000")
                .address("Mountain View, California & Bangalore, India")
                .tier("Super Dream")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/2/2f/Google_2015_logo.svg")
                .build());

        Company microsoft = companyRepository.save(Company.builder()
                .name("Microsoft")
                .industry("Software & Cloud Computing")
                .website("https://careers.microsoft.com")
                .description("Microsoft enables digital transformation for the era of an intelligent cloud and an intelligent edge.")
                .contactPerson("Ananya Sen")
                .contactEmail("campus-india@microsoft.com")
                .contactPhone("+91 80 6789 0000")
                .address("Redmond, WA & Hyderabad / Bangalore, India")
                .tier("Super Dream")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/9/96/Microsoft_logo_%282012%29.svg")
                .build());

        Company amazon = companyRepository.save(Company.builder()
                .name("Amazon")
                .industry("E-Commerce & Cloud (AWS)")
                .website("https://amazon.jobs")
                .description("Amazon is guided by four principles: customer obsession, passion for invention, commitment to operational excellence.")
                .contactPerson("David Miller")
                .contactEmail("aws-campus@amazon.com")
                .contactPhone("+91 80 4150 0000")
                .address("Seattle, WA & Bangalore / Chennai, India")
                .tier("Super Dream")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg")
                .build());

        Company deloitte = companyRepository.save(Company.builder()
                .name("Deloitte")
                .industry("Consulting & IT Advisory")
                .website("https://deloitte.com/careers")
                .description("Deloitte provides audit, consulting, financial advisory, risk management, and tax services.")
                .contactPerson("Kavita Rao")
                .contactEmail("deloitte-campus@deloitte.com")
                .contactPhone("+91 22 6185 4000")
                .address("Mumbai / Hyderabad, India")
                .tier("Core")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/5/56/Deloitte.svg")
                .build());

        Company infosys = companyRepository.save(Company.builder()
                .name("Infosys")
                .industry("Information Technology & Services")
                .website("https://infosys.com/careers")
                .description("Infosys is a global leader in next-generation digital services and consulting.")
                .contactPerson("Amitabh Das")
                .contactEmail("campus-connect@infosys.com")
                .contactPhone("+91 80 2852 0261")
                .address("Electronic City, Bangalore, India")
                .tier("Mass")
                .logoUrl("https://upload.wikimedia.org/wikipedia/commons/9/95/Infosys_logo.svg")
                .build());

        // 6. Job Postings
        JobPosting googleSde = jobPostingRepository.save(JobPosting.builder()
                .title("Software Development Engineer I (SDE 1)")
                .company(google)
                .jobType(JobType.FULL_TIME)
                .role("Full-Stack / Backend Engineer")
                .location("Bangalore / Hyderabad")
                .ctcLpa(24.5)
                .description("Join Google's core engineering organization working on large-scale distributed systems, web architectures, and algorithms.")
                .requirements("Strong knowledge of Data Structures, Algorithms, System Design basics, Java/C++/Go.")
                .minCgpa(8.0)
                .minTenthPercentage(80.0)
                .minTwelfthPercentage(80.0)
                .maxBacklogsAllowed(0)
                .allowedBranches("CSE,IT")
                .targetPassingYear(2025)
                .rounds("Online Assessment -> Technical Interview 1 -> Technical Interview 2 -> Googliness & Leadership")
                .applicationDeadline(LocalDate.now().plusDays(20))
                .driveDate(LocalDate.now().plusDays(25))
                .driveStatus(DriveStatus.ACTIVE)
                .build());

        JobPosting msftCloud = jobPostingRepository.save(JobPosting.builder()
                .title("Cloud Solution & DevOps Engineer")
                .company(microsoft)
                .jobType(JobType.FULL_TIME)
                .role("Cloud / Systems Associate")
                .location("Hyderabad / Noida")
                .ctcLpa(18.0)
                .description("Empower enterprises to build modern architectures on Azure cloud infrastructure.")
                .requirements("Familiarity with Cloud services, Networking, Linux, Python/Java scripting, CI/CD.")
                .minCgpa(7.5)
                .minTenthPercentage(75.0)
                .minTwelfthPercentage(75.0)
                .maxBacklogsAllowed(0)
                .allowedBranches("CSE,IT,ECE")
                .targetPassingYear(2025)
                .rounds("Online Coding Assessment -> Technical Round -> System Design & Azure Basics -> HR")
                .applicationDeadline(LocalDate.now().plusDays(15))
                .driveDate(LocalDate.now().plusDays(22))
                .driveStatus(DriveStatus.ACTIVE)
                .build());

        JobPosting amazonIntern = jobPostingRepository.save(JobPosting.builder()
                .title("Software Development Intern (6 Months)")
                .company(amazon)
                .jobType(JobType.INTERNSHIP)
                .role("SDE Intern")
                .location("Bangalore / Remote")
                .stipendPerMonth(65000.0)
                .ctcLpa(16.0)
                .description("6-month full-time internship with potential PPO (Pre-Placement Offer) conversion for high performers.")
                .requirements("Proficiency in object-oriented programming (Java, C++, or Python), problem-solving skills.")
                .minCgpa(7.0)
                .minTenthPercentage(70.0)
                .minTwelfthPercentage(70.0)
                .maxBacklogsAllowed(0)
                .allowedBranches("ALL")
                .targetPassingYear(2025)
                .rounds("Amazon HackerRank Test -> Technical Round 1 -> Technical Round 2")
                .applicationDeadline(LocalDate.now().plusDays(10))
                .driveDate(LocalDate.now().plusDays(14))
                .driveStatus(DriveStatus.ACTIVE)
                .build());

        JobPosting deloitteAnalyst = jobPostingRepository.save(JobPosting.builder()
                .title("Technology Advisory & Risk Analyst")
                .company(deloitte)
                .jobType(JobType.FULL_TIME)
                .role("Technology Analyst")
                .location("Mumbai / Gurgaon")
                .ctcLpa(10.5)
                .description("Work on IT strategy, cybersecurity advisory, and enterprise application implementations for Fortune 500 clients.")
                .requirements("Analytical mindset, communication skills, basics of databases, software lifecycles.")
                .minCgpa(6.5)
                .minTenthPercentage(65.0)
                .minTwelfthPercentage(65.0)
                .maxBacklogsAllowed(1)
                .allowedBranches("ALL")
                .targetPassingYear(2025)
                .rounds("Aptitude & Case Assessment -> Technical Interview -> Partner / HR Round")
                .applicationDeadline(LocalDate.now().plusDays(30))
                .driveDate(LocalDate.now().plusDays(35))
                .driveStatus(DriveStatus.ACTIVE)
                .build());

        JobPosting infosysSp = jobPostingRepository.save(JobPosting.builder()
                .title("Specialist Programmer & Systems Engineer")
                .company(infosys)
                .jobType(JobType.FULL_TIME)
                .role("Specialist Programmer")
                .location("Pan India")
                .ctcLpa(9.5)
                .description("Develop advanced architectures in full stack, cloud, data engineering, and automation.")
                .requirements("Hands-on coding experience, familiarity with web frameworks and databases.")
                .minCgpa(6.0)
                .minTenthPercentage(60.0)
                .minTwelfthPercentage(60.0)
                .maxBacklogsAllowed(2)
                .allowedBranches("ALL")
                .targetPassingYear(2025)
                .rounds("InfyTQ / HackWithInfy -> Technical Interview -> HR Round")
                .applicationDeadline(LocalDate.now().plusDays(25))
                .driveDate(LocalDate.now().plusDays(28))
                .driveStatus(DriveStatus.ACTIVE)
                .build());

        // 7. Applications
        // Rahul applies to Google (Shortlisted -> Tech Round)
        Application rahulGoogleApp = applicationRepository.save(Application.builder()
                .student(rahulProfile)
                .jobPosting(googleSde)
                .status(ApplicationStatus.TECHNICAL_ROUND)
                .currentRound("Technical Interview 1")
                .coverNote("Experienced with high-performance Spring Boot services and algorithmic problem solving. Solved 450+ LeetCode problems.")
                .officerNotes("Cleared Online Assessment with 100% score. Recommended for fast-track interview.")
                .build());

        // Rahul applies to Amazon Intern (Applied)
        applicationRepository.save(Application.builder()
                .student(rahulProfile)
                .jobPosting(amazonIntern)
                .status(ApplicationStatus.APPLIED)
                .currentRound("Applied / Initial Review")
                .coverNote("Enthusiastic about distributed cloud solutions and AWS architectures.")
                .build());

        // Priya applies to Microsoft Cloud (Selected / Offer Released)
        applicationRepository.save(Application.builder()
                .student(priyaProfile)
                .jobPosting(msftCloud)
                .status(ApplicationStatus.SELECTED)
                .currentRound("Offer Extended")
                .coverNote("Deep passion for AI systems and scalable cloud infrastructure.")
                .officerNotes("Outstanding performance in system design and cultural fit round.")
                .offeredCtc(18.0)
                .offerAccepted(true)
                .build());

        // Arjun applies to Deloitte Analyst (Under Review)
        applicationRepository.save(Application.builder()
                .student(arjunProfile)
                .jobPosting(deloitteAnalyst)
                .status(ApplicationStatus.UNDER_REVIEW)
                .currentRound("Resume Screening")
                .coverNote("Bringing strong analytical rigor and systems understanding.")
                .build());

        // 8. Interview Schedules
        interviewScheduleRepository.save(InterviewSchedule.builder()
                .application(rahulGoogleApp)
                .roundName("Google Technical Interview 1 - Data Structures & Algorithms")
                .interviewDate(LocalDate.now().plusDays(3))
                .interviewTime(LocalTime.of(14, 30))
                .mode(InterviewMode.ONLINE)
                .meetingLinkOrVenue("https://meet.google.com/xyz-placement-2025")
                .interviewerName("Kiran Reddy (Senior Staff SDE, Google)")
                .interviewerEmail("kiranreddy@google.com")
                .instructions("Please join 5 minutes early. Keep photo ID ready and ensure a stable broadband connection with Google Docs/CoderPad enabled.")
                .status("SCHEDULED")
                .build());

        // 9. Campus Placement Announcements
        announcementRepository.save(Announcement.builder()
                .title("🚀 Google Campus Recruitment Drive 2025 Announced!")
                .content("Google has officially scheduled their campus hiring drive for 2025 batch. Eligible branches: CSE & IT. Ensure your profiles and resumes are updated before the deadline!")
                .targetAudience("CSE,IT")
                .postedBy("Prof. Rajesh Sharma")
                .priority("URGENT")
                .active(true)
                .build());

        announcementRepository.save(Announcement.builder()
                .title("📝 Resume Verification & Placement Portal Guidelines")
                .content("All students are requested to ensure their 10th, 12th, and current CGPA details match official transcripts. Incorrect backlogs declaration will result in de-registration from drives.")
                .targetAudience("ALL")
                .postedBy("Placement Cell")
                .priority("NORMAL")
                .active(true)
                .build());

        announcementRepository.save(Announcement.builder()
                .title("💡 Pre-Placement Talk: Amazon SDE & Cloud Internship")
                .content("Amazon campus recruitment team will conduct a virtual Pre-Placement Talk this Friday at 4:00 PM via Amazon Chime. Link will be circulated to shortlisted applicants.")
                .targetAudience("ALL")
                .postedBy("Training & Placement Office")
                .priority("HIGH")
                .active(true)
                .build());

        log.info("Seed Data Initialization Completed Successfully!");
    }
}
