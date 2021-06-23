package cn.looyeagee.heo;

import cn.looyeagee.heo.entity.User;
import cn.looyeagee.heo.mapper.CommentMapper;
import cn.looyeagee.heo.mapper.UserMapper;
import cn.looyeagee.heo.service.CommentService;
import cn.looyeagee.heo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Base64;

@SpringBootTest
class HeoApplicationTests {
	@Resource
	CommentMapper commentMapper;
	@Resource
	CommentService commentService;
	@Resource
	UserService userService;
	@Test
	void t() {
		System.out.println(commentMapper.getByOrderId(32));
	}


	@Test
	void delComment(){
		System.out.println(commentService.del(6, 1));
	}
	@Test
	void soutKey(){
		String str = "周锐淇";
		//对原始文本进行Base64解码
//		byte[] decodedKey1 = Base64.getDecoder().decode(str.getBytes());
		byte[] encodedKey = Base64.getEncoder().encode(str.getBytes());
		byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		System.out.println("******************************");
		System.out.println(str);
//		System.out.println(new String(decodedKey1));//乱码
		System.out.println(new String(encodedKey));//加密后数据
		System.out.println(new String(decodedKey));//周锐淇
	}

}
