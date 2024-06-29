package org.example.bookinghotel.controller;

import jakarta.validation.Valid;
import org.example.bookinghotel.dto.BookingRoomDto;
import org.example.bookinghotel.model.BookedRoom;
import org.example.bookinghotel.model.Role;
import org.example.bookinghotel.model.Room;
import org.example.bookinghotel.model.User;
import org.example.bookinghotel.service.BookedRoomService;
import org.example.bookinghotel.service.PaginateRequest;
import org.example.bookinghotel.service.RoomService;
import org.example.bookinghotel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    RoomService roomService;
    @Autowired
    BookedRoomService bookedRoomService;
    @Value("${file-upload}")
    private String upload;
    @GetMapping
    public ModelAndView homeAdmin(){
        ModelAndView modelAndView = new ModelAndView("/admin/home");
        return modelAndView;
    }
    @GetMapping("/showAllUser")
    public ModelAndView showAllUser(@PageableDefault(value = 5)Pageable pageable,
                                        @RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "email", required = false) String email,
                                        @RequestParam(value = "gender", required = false) String gender,
                                        @RequestParam(value = "numberPhone", required = false) String numberPhone,
                                        @RequestParam(value = "sortDir", required = false)String sortDir,
                                        @RequestParam(value = "sortField", required = false)String sortField){
        ModelAndView modelAndView = new ModelAndView("/admin/list/listUser");

        if (username == null && email ==null && gender == null && numberPhone == null){
            modelAndView.addObject("listUser", userService.findAll(pageable));
            return modelAndView;
        }

        Page<User> userPage = userService.findAll(new PaginateRequest(pageable.getPageNumber(), pageable.getPageSize()), username, email, gender, numberPhone, sortField, sortDir);
        modelAndView.addObject("username", username);
        modelAndView.addObject("email", email);
        modelAndView.addObject("gender", gender);
        modelAndView.addObject("numberPhone", numberPhone);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("listUser", userPage);
        return modelAndView;
    }

    @GetMapping("/showAllRoom")
    public ModelAndView showAllRoom(@PageableDefault(value = 5)Pageable pageable,
                                        @RequestParam(value = "roomType", required = false) String roomType,
                                        @RequestParam(value = "roomPrice", required = false) Long roomPrice,
                                        @RequestParam(value = "sortDir", required = false)String sortDir,
                                        @RequestParam(value = "sortField", required = false)String sortField){
        ModelAndView modelAndView = new ModelAndView("/admin/list/listRoom");

        if (roomPrice == null && roomType ==null){
            modelAndView.addObject("listRoom", roomService.findAll(pageable));
            return modelAndView;
        }

        if (roomPrice == null){
            roomPrice = 10000000000L;
        }


        Page<Room> roomPage = roomService.findAll(new PaginateRequest(pageable.getPageNumber(), pageable.getPageSize()), roomType, roomPrice, sortField, sortDir);
        modelAndView.addObject("roomType", roomType);
        modelAndView.addObject("description", "description");
        modelAndView.addObject("roomPrice", roomPrice);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("listRoom", roomPage);
        return modelAndView;
    }

    @GetMapping("/showAllBooking")
    public ModelAndView showAllBooking(@PageableDefault(value = 5)Pageable pageable,
                                       @RequestParam(value = "checkIn", required = false) LocalDate checkIn,
                                       @RequestParam(value = "checkOut", required = false) LocalDate checkOut,
                                       @RequestParam(value = "sortDir", required = false)String sortDir,
                                       @RequestParam(value = "sortField", required = false)String sortField){
        ModelAndView modelAndView = new ModelAndView("/admin/list/listBooking");

        if (checkIn == null && checkOut == null){
            modelAndView.addObject("listBooking", bookedRoomService.findAll(pageable));
            return modelAndView;
        }



        Page<BookedRoom> bookedRooms = bookedRoomService.findAll(new PaginateRequest(pageable.getPageNumber(), pageable.getPageSize()), checkIn, checkOut, sortField, sortDir);
        modelAndView.addObject("checkIn", checkIn);
        modelAndView.addObject("checkOut", checkOut);
        modelAndView.addObject("roomId", "room");
        modelAndView.addObject("userId", "user");
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("listBooking", bookedRooms);
        return modelAndView;
    }
    @GetMapping("createBooking/{idRoom}")
    public ModelAndView formCreateBooking(@PathVariable("idRoom") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/create/CreateBooking");
        modelAndView.addObject("user", new User());
        modelAndView.addObject("room", roomService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("createBooking/{idRoom}")
    public ModelAndView createBooking(@Valid @ModelAttribute BookingRoomDto bookingRoomDto,
                                      @PathVariable("idRoom") Long idRoom,
                                      RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllBooking");
        Room room = roomService.findById(idRoom).get();
        if (room.isBooked() || bookedRoomService.
                        checkDate(bookingRoomDto.getCheckInDate(), bookingRoomDto.getCheckOutDate())){
            redirectAttributes.addFlashAttribute("msg", "That bai");
            redirectAttributes.addFlashAttribute("status", "error");
        }else {
            BookedRoom bookedRoom = bookedRoomService.toBookedRoom(bookingRoomDto);
            redirectAttributes.addFlashAttribute("msg", "Thành công");
            bookedRoomService.save(bookedRoom);
        }
        return modelAndView;
    }
    @GetMapping("updateBooking/{id}")
    public ModelAndView formUpdateBooking(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/update/UpdateBooking");
        modelAndView.addObject("booking", bookedRoomService.findById(id).get());
        return modelAndView;
    }
    @PostMapping("updateBooking/{id}")
    public ModelAndView updateBooking(@Valid @ModelAttribute BookingRoomDto bookingRoomDto,
                                      @PathVariable("id") Long id,
                                      RedirectAttributes redirectAttributes){
        BookedRoom bookedRoom = bookedRoomService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllBooking");
        if (bookedRoomService.
                checkDate(bookingRoomDto.getCheckInDate(), bookingRoomDto.getCheckOutDate())){
            redirectAttributes.addFlashAttribute("msg", "That bai");
            redirectAttributes.addFlashAttribute("status", "error");
        }else {
            bookingRoomDto.setIdRoom(bookedRoom.getRoom().getRoomId());
            BookedRoom bookedRoom1 = bookedRoomService.toBookedRoom(bookingRoomDto);
            bookedRoom.setCheckInDate(bookedRoom1.getCheckInDate());
            bookedRoom.setCheckOutDate(bookedRoom1.getCheckOutDate());
            bookedRoom.setUser(bookedRoom1.getUser());
            redirectAttributes.addFlashAttribute("msg", "Thành công");
            bookedRoomService.save(bookedRoom);
        }
        return modelAndView;
    }
    @PostMapping("/deleteRoom")
    public ModelAndView deleteRoom(@RequestParam(value = "id", required = false)Long id,
                                   RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllRoom");
        redirectAttributes.addFlashAttribute("msg", "Xóa room thành công");
        roomService.delete(id);
        return modelAndView;
    }

    @PostMapping("/deleteUser")
    public ModelAndView deleteUser(@RequestParam(value = "id", required = false)Long id,
                                   RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllUser");
        redirectAttributes.addFlashAttribute("msg", "Thành công");
        userService.delete(id);
        return modelAndView;
    }
    @PostMapping("/deleteBooking")
    public ModelAndView deleteBooking(@RequestParam(value = "id", required = false)Long id,
                                   RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllBooking");
        redirectAttributes.addFlashAttribute("msg", "Thành công");
        bookedRoomService.delete(id);
        return modelAndView;
    }
    @GetMapping("/editRoom/{id}")
    public ModelAndView formEditRoom(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/update/UpdateRoom");
        modelAndView.addObject("room", roomService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("/editRoom/{id}")
    public ModelAndView editRoom(@RequestParam(value = "file", required = false) MultipartFile file,
                                 @PathVariable Long id,
                                 @Valid Room room,
                                 RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView modelAndView;
        String fileName = file.getOriginalFilename();
        if (fileName.isEmpty()){
            modelAndView = new ModelAndView("redirect:/admin/showAllRoom");
            redirectAttributes.addFlashAttribute("msg", "Sửa room thành công");
            room.setRoomId(id);
            roomService.save(room);
            return modelAndView;
        }else {
            modelAndView = new ModelAndView("redirect:/admin/showAllRoom");
            FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
            redirectAttributes.addFlashAttribute("msg", "Sửa room thành công");
            room.setDirectionImage("http://localhost:8080/image/" + fileName);
            room.setRoomId(id);
            roomService.save(room);
            return modelAndView;
        }
    }
    @GetMapping("/createRoom")
    public ModelAndView formCreateRoom(){
        ModelAndView modelAndView = new ModelAndView("/admin/create/CreateRoom");
        modelAndView.addObject("room", new Room());
        return modelAndView;
    }
    @PostMapping("/createRoom")
    public ModelAndView addNewRoom(@RequestParam(value = "file", required = false) MultipartFile file,
                                   @Valid Room room,
                                   RedirectAttributes redirectAttributes) throws IOException {
        ModelAndView modelAndView;
        String fileName = file.getOriginalFilename();
        if (fileName.isEmpty()){
            modelAndView = new ModelAndView("redirect:/admin/showAllRoom");
            redirectAttributes.addFlashAttribute("msg", "Thành công");
            roomService.save(room);
            return modelAndView;
        }else {
            modelAndView = new ModelAndView("redirect:/admin/showAllRoom");
            redirectAttributes.addFlashAttribute("msg", "Thành công");
            FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
            room.setDirectionImage("http://localhost:8080/image/" + fileName);
            roomService.save(room);
            return modelAndView;
        }
    }
    @GetMapping("/viewUser/{id}")
    public ModelAndView viewUser(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/view/ViewUser");
        modelAndView.addObject("user", userService.findById(id).get());
        return modelAndView;
    }
    @GetMapping("/viewRoom/{id}")
    public ModelAndView viewRoom(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/view/ViewRoom");
        modelAndView.addObject("room", roomService.findById(id).get());
        return modelAndView;
    }
    @GetMapping("/viewBooking/{id}")
    public ModelAndView viewBooking(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/view/ViewBooking");
        modelAndView.addObject("booking", bookedRoomService.findById(id).get());
        return modelAndView;
    }
    @GetMapping("/updateUser/{id}")
    public ModelAndView formUpdateUser(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/admin/update/UpdateUser");
        modelAndView.addObject("user", userService.findById(id).get());
        return modelAndView;
    }
    @PostMapping("/updateUser/{id}")
    public ModelAndView updateUser(@PathVariable("id") Long id, @Valid User user, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllUser");
        if (userService.findById(id).isPresent()){
            userService.save(user);
        }
        redirectAttributes.addFlashAttribute("msg", "Sửa user thành công");
        return modelAndView;
    }
    @GetMapping("/createUser")
    public ModelAndView formCreateUser(){
        ModelAndView modelAndView = new ModelAndView("/admin/create/CreateUser");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/createUser")
    public ModelAndView createUser(@Valid User user, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/showAllUser");
        Role role = new Role();
        user.setRole(role);
        user.getRole().setId(2);
        userService.save(user);
        redirectAttributes.addFlashAttribute("msg", "Thành công");
        return modelAndView;
    }
}
