package com.sangeng;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.*;
import com.sangeng.domain.vo.CommentVo;
import com.sangeng.job.UpdateViewCountJob;
import com.sangeng.mapper.ArticleMapper;
import com.sangeng.mapper.ArticleTagMapper;
import com.sangeng.service.*;
import com.sangeng.utils.RedisCache;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
public class jwtTest {

    private long time = 1000*60*60*24;

    private String signature = "admin";

    @Test
    public void jwt(){
        //用来构建jwt对象
        JwtBuilder jwtBuilder = Jwts.builder();
        //创建jwt的三部分
        String jwtToken = jwtBuilder
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //payload
                .claim("username","jerry")
                .claim("role","admin")
                .setSubject("admin-test")       //定义主题
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        System.out.println(jwtToken);
    }

    @Test
    public void pares(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImplcnJ5Iiwicm9sZSI6ImFkbWluIiwic3ViIjoiYWRtaW4tdGVzdCIsImV4cCI6MTY1NjIyMDQ2MywianRpIjoiZGFlYjk4OWUtYWU0Ny00NzUxLTkyMjEtMWI5MDBmYWIzNzM1In0.xj7VL0ccIGVIHlieFvsQMFxYLXH0cyDF5us_0M6iDWA";
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println("-----------------------------");
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.get("id"));
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());
        System.out.println("-------------------------------------");

    }

    @Test
    public void getInfo(){
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("---------------");
        System.out.println(admin);
        System.out.println("-----------------");
    }


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;


    @Test
    public void getArticleTest(){
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.VIEW_COUNT);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);
    }

    @Autowired
    private CategoryService categoryService;

    @Test
    void tTest(){
        Category category = new Category();
        category.setId(2L);
        category.setStatus("2");
        categoryService.updateById(category);
        System.out.println("success");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void tmTest(){
//        articleService.updateById(article);
//        System.out.println("success");
//        User user = new User();
//        user.setStatus("1");
//        user.setId(5L);
//        userService.updateUser(user);
//        System.out.println("success");
        Article byId = articleService.getById(1L);
        byId.setViewCount(202L);
        articleService.updateById(byId);
        System.out.println("success");
    }

    @Autowired
    private RoleService roleService;
    @Test
    void commnetTest(){
        Role byId = roleService.getById(11L);
        System.out.println("success");
    }


}
