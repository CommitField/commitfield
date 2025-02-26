//package cmf.commitField.domain.chat.chatMessage.controller;
//
//import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
//import cmf.commitField.domain.chat.chatMessage.repository.ChatMessageCustomRepository;
//import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
//import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
//import cmf.commitField.domain.user.entity.CustomOAuth2User;
//import cmf.commitField.domain.user.entity.User;
//import cmf.commitField.domain.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//
//import java.util.Optional;
//
//@Controller
//@RequiredArgsConstructor
//public class ChatController {
//
//    private final ChatMessageCustomRepository chatMessageRepository;
//    private final ChatRoomRepository chatRoomRepository;
//    private final UserRepository userRepository;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/chat.sendMessage") // 클라이언트에서 "/app/chat.sendMessage"로 보낼 때 처리됨
//    public void sendMessage(ChatMsg chatMsg) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated() &&
//                authentication.getPrincipal() instanceof CustomOAuth2User) {
//
//            CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
//            Optional<User> userOptional = userRepository.findByUsername(oauth2User.getName());
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//                Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(chatMsg.getChatRoom().getId());
//
//                if (chatRoomOptional.isPresent()) {
//                    ChatRoom chatRoom = chatRoomOptional.get();
//                    chatMsg.setUser(user);
//                    chatMsg.setChatRoom(chatRoom);
//
//                    ChatMsg savedMessage = chatMessageRepository.save(chatMsg);
//
//                    // 채팅방 구독자들에게 메시지 전송
//                    messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getId(), savedMessage);
//                }
//            }
//        }
//    }
//}
