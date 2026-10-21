package com.ticketShop.http;

import com.ticketShop.model.dto.UserDTO;
import com.ticketShop.model.enums.UserStatus;
import com.ticketShop.model.enums.ResultUtil;
import com.ticketShop.model.vo.ResultMessage;
import com.ticketShop.service.user.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserAppService userAppService;

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin tài khoản hiện tại")
    public ResultMessage<UserDTO> getCurrentUser(@RequestAttribute("userId") Long userId) {
        log.info("API Request - Get current user profile ID: {}", userId);
        UserDTO userDTO = userAppService.getCurrentUser(userId);
        return ResultUtil.data(userDTO);
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách người dùng có phân trang và lọc theo trạng thái")
    public ResultMessage<Page<UserDTO>> getUsersByStatus(
            @Parameter(description = "Trạng thái người dùng (INACTIVE, ACTIVE, BLOCKED)")
            @RequestParam(name = "status", required = false) UserStatus status,

            @Parameter(description = "Số trang (bắt đầu từ 0)")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(description = "Số lượng bản ghi trên một trang")
            @RequestParam(name = "size", defaultValue = "10") int size,

            @Parameter(description = "Sắp xếp theo trường (vd: createdAt, username)")
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Chiều sắp xếp (ASC hoặc DESC)")
            @RequestParam(name = "direction", defaultValue = "DESC") String direction
    ) {
        log.info("API Request - Get users list with status: {}", status);

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserDTO> resultPage = userAppService.getUsersByStatus(status, pageable);
        return ResultUtil.data(resultPage);
    }
}