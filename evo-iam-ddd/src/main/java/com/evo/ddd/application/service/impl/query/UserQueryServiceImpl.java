package com.evo.ddd.application.service.impl.query;

import com.evo.common.UserAuthority;

import com.evo.ddd.application.dto.mapper.UserDTOMapper;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.mapper.QueryMapper;
import com.evo.ddd.application.service.UserQueryService;
import com.evo.ddd.domain.Permission;
import com.evo.ddd.domain.Role;
import com.evo.ddd.domain.User;
import com.evo.ddd.domain.UserRole;
import com.evo.ddd.domain.query.SearchUserQuery;
import com.evo.ddd.domain.repository.RoleDomainRepository;
import com.evo.ddd.domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserDomainRepository userDomainRepository;
    private final UserDTOMapper userDTOMapper;
    private final QueryMapper queryMapper;
    private final RoleDomainRepository roleDomainRepository;


    @Override
    public UserDTO getUserInfo(String username) {
        User user = userDomainRepository.getByUsername(username);
        return userDTOMapper.domainModelToDTO(user);
    }

    @Override
    public Long totalUsers(SearchUserRequest request) {
        SearchUserQuery searchUserQuery = queryMapper.from(request);
        return userDomainRepository.count(searchUserQuery);
    }

    @Override
    public List<UserDTO> search(SearchUserRequest request) {
        SearchUserQuery searchUserQuery = queryMapper.from(request);
        List<User> users = userDomainRepository.search(searchUserQuery);
        return users.stream().map(userDTOMapper::domainModelToDTO).toList();
    }

    @Override
    public byte[] exportUserListToExcel(SearchUserRequest searchUserRequest) {
        List<UserDTO> users = search(searchUserRequest);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");
            Row header = sheet.createRow(0);
            header.setHeightInPoints(25); //Tăng chiều cao tiêu đề

            String[] columns = {"STT", "Full Name", "Username", "Email", "DoB", "Address", " Years of Experience"};

            // Định dạng tiêu đề: Nền xanh dương, chữ trắng, in đậm, cỡ chữ lớn
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex()); // Chữ trắng
            headerFont.setFontHeightInPoints((short) 14); //Tăng cỡ chữ tiêu đề
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex()); // Nền xanh dương
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.MEDIUM);
            headerStyle.setBorderBottom(BorderStyle.MEDIUM);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Style cho cell
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            // Tạo tiêu đề với style
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (UserDTO user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.setHeightInPoints(20); // Tăng chiều cao dòng dữ liệu
                row.createCell(0).setCellValue(rowIdx - 1);
                row.createCell(1).setCellValue(user.getFirstName() + " " + user.getLastName());
                row.createCell(2).setCellValue(user.getUsername());
                row.createCell(3).setCellValue(user.getEmail());

                //Xử lý Date thành String với format dd/MM/yyyy
                String formattedDate = (user.getDateOfBirth() != null) ? dateFormat.format(user.getDateOfBirth()) : "";
                row.createCell(4).setCellValue(formattedDate);

                row.createCell(5).setCellValue(user.getAddress());
                row.createCell(6).setCellValue(user.getYearsOfExperience());

                for (int i = 0; i < columns.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512); // Thêm 2 đơn vị (~2 ký tự)
            }

            // Xuất file ra mảng byte[]
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthority getUserAuthority(String username) {
        User user = userDomainRepository.getByUsername(username);
        List<UserRole> userRoles = user.getUserRole();

        List<Role> roles = userRoles.stream()
                .map(userRole -> roleDomainRepository.getById(userRole.getRoleId()))
                .toList();

        boolean isRoot = roles.stream().anyMatch(Role::isRoot);

        List<Permission> allPermissions = roles.stream()
                .flatMap(role -> roleDomainRepository.findPermissionByRoleId(role.getId()).stream())
                .toList();

        List<String> grantedPermissions = allPermissions.stream()
                .filter(Objects::nonNull)
                .map(permission -> permission.getResource() + "." + permission.getScope())
                .toList();
        return UserAuthority.builder().userId(user.getUserID()).isRoot(isRoot).grantedPermissions(grantedPermissions).build();
    }

//    @Override
//    public UserAuthority getClientAuthority(String clientId) {
//        OauthClient oauthClient = oauthClientDomainRepository.findByClientId(clientId);
//        return UserAuthority.builder().userId(oauthClient.getId()).isRoot(false).isClient(true).build();
//    }
}
