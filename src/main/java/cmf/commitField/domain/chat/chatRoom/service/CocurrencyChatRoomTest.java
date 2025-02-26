//package cmf.commitField.domain.chat.chatRoom.service;
//
//import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
//import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
//import cmf.commitField.domain.chat.userChatRoom.repository.UserChatRoomRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.stream.IntStream;
//
//@SpringBootTest
//public class ConcurrencyChatRoomTest {
//    ExecutorService executorService;
//    CountDownLatch countDownLatch;
//
//    @BeforeEach
//    void beforeEach() {
//        executorService = Executors.newFixedThreadPool(30);
//        countDownLatch = new CountDownLatch(30);
//    }
//
//    @Autowired
//    private ChatRoomService chatRoomService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserChatRoomRepository userChatRoomRepository;
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Test
//    @DisplayName("채팅방에 최대 정원 100명, 30명 인원 한번에 입장 시도(lock)")
//    void joinRoom_lock_success() throws InterruptedException {
//        // 동시성 체크
//        List<User> users = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            UserRequest user = new UserRequest(i + "아이디", "12345", "test");
//            User user1 = userService.signUp(user);
//            users.add(user1);
//        }
//
//        ChatRoom test = ChatRoom.builder()
//                .roomCreator(1L)
//                .title("test")
//                .userCountMax(100)
//                .build();
//        ChatRoom save = chatRoomRepository.save(test);
//
//        IntStream.range(0, 30).forEach(e -> executorService.submit(() -> {
//            try {
//                // 테스트할코드
//                try {
//                    chatRoomService.joinRoom(save.getId(), users.get(e).getId());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            } finally {
//                countDownLatch.countDown();
//            }
//        }));
//        countDownLatch.await();
//
//        Long aLong = userChatRoomRepository.countNonLockByChatRoomId(save.getId());
//
//        assertThat(aLong).isEqualTo(8);
//    }
//}
