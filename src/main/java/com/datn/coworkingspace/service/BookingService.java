package com.datn.coworkingspace.service;

import com.datn.coworkingspace.dto.BookingDTO;
import com.datn.coworkingspace.dto.BookingStatusDTO;
import com.datn.coworkingspace.dto.MessageResponse;
import com.datn.coworkingspace.entity.Booking;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.entity.SubSpace;
import com.datn.coworkingspace.entity.User;
import com.datn.coworkingspace.enums.BookingStatus;
import com.datn.coworkingspace.exception.ResourceNotFoundException;
import com.datn.coworkingspace.repository.BookingRepository;
import com.datn.coworkingspace.repository.SpaceRepository;
import com.datn.coworkingspace.repository.SubSpaceRepository;
import com.datn.coworkingspace.repository.UserRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingService implements  IBookingService{

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubSpaceRepository subSpaceRepository;

    @Override
    public Page<Booking> findAllPageAndSort(Pageable pagingSort) {
        Page<Booking> bookingPage =  bookingRepository.findAll(pagingSort);

        return  getBookings(bookingPage);
    }

    @Override
    public Booking findById(Long theId)throws ResourceNotFoundException {
        Optional<Booking> booking = bookingRepository.findById(theId);
        if(!booking.isPresent()) {
            throw  new ResourceNotFoundException("Not found booking with ID=" + theId);
        } else {
            return booking.get();
        }
    }

    @Override
    public MessageResponse createBooking(BookingDTO theBookingDto) {
        Optional<User> user = userRepository.findByIdCustomer(theBookingDto.getUserId());
        if(!user.isPresent()) {
            return new MessageResponse("Not found customer with ID=" + theBookingDto.getUserId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<Space> space = spaceRepository.findById(theBookingDto.getSpaceId());
        if(!space.isPresent()) {
            return new MessageResponse("Not found space with ID=" + theBookingDto.getSpaceId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<SubSpace> subSpace = subSpaceRepository.findById(theBookingDto.getSubSpaceId());
        if(!subSpace.isPresent()) {
            return new MessageResponse("Not found sub space with ID=" + theBookingDto.getSubSpaceId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        if(!subSpace.get().isStatus()) {
            return new MessageResponse("Sub Space not available", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(subSpace.get().getNumberOfPeople() < theBookingDto.getNumberOfPeople()) {
            return new MessageResponse("Maximum number of people is " + subSpace.get().getNumberOfPeople(), HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(!BookingStatus.PENDING.equals(theBookingDto.getStatus())) {
            return new MessageResponse("Error when create booking!" , HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        Booking booking = new Booking();
        booking.setName(theBookingDto.getName());
        booking.setEmail(theBookingDto.getEmail());
        booking.setCompanyName(theBookingDto.getCompanyName());
        booking.setPhoneNumber(theBookingDto.getPhoneNumber());
        booking.setNumberOfPeople(theBookingDto.getNumberOfPeople());
        booking.setTotalPrice(theBookingDto.getTotalPrice());
        booking.setStartDate(theBookingDto.getStartDate());
        booking.setNumberTimePerUnit(theBookingDto.getNumberTimePerUnit());
        booking.setNote(theBookingDto.getNote());
        booking.setCreatedBy(theBookingDto.getName());
        booking.setStatus(theBookingDto.getStatus());
        booking.setCreatedDate(new Date());
        booking.setSubSpace(subSpace.get());
        booking.setUser(user.get());
        booking.setSpaceId(theBookingDto.getSpaceId());

        subSpace.get().setModifiedBy(user.get().getName());
        subSpace.get().setModifiedDate(new Date());
        subSpace.get().setStatus(false);

        subSpaceRepository.save(subSpace.get());

        bookingRepository.save(booking);

        return new MessageResponse("Create booking successfully!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public MessageResponse updateBooking(Long theId, BookingDTO theBookingDto) {
        return new MessageResponse("TODO ^-^!", HttpStatus.CREATED, LocalDateTime.now());
    }

    @Override
    public Page<Booking> findBySearchContentContaining(String content, String status, Pageable pagingSort) {
        Page<Booking> bookingPage;

        content = content == null ? "" : content;
        try {
            if(status == null) {
                bookingPage = bookingRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(content, content, content, content, pagingSort);
            } else  {
                if(EnumUtils.isValidEnum(BookingStatus.class, status)) {
                    bookingPage = bookingRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(content, content, content, content, pagingSort);
                    List<Booking> bookings = bookingPage.getContent().stream().filter(x -> x.getStatus().equals(EnumUtils.getEnum(BookingStatus.class, status))).collect(Collectors.toList());

                    bookingPage = new PageImpl<>(bookings, pagingSort, bookings.size());
                } else {
                    return new PageImpl<>(new ArrayList<>(), pagingSort, 0);
                }
            }
            return getBookings(bookingPage);
        } catch (Exception e) {
            e.printStackTrace();
            return new PageImpl<>(new ArrayList<>(), pagingSort, 0);
        }

    }

    @Override
    public MessageResponse updateStatusBooking(long theId, BookingStatusDTO statusDto) {
        Optional<Booking> booking  = bookingRepository.findById(theId);

        if(!booking.isPresent()) {
            return new MessageResponse("Not found booking  with ID=" + theId, HttpStatus.NOT_FOUND, LocalDateTime.now());
        }
        Optional<User> user = userRepository.findById(statusDto.getUserId());
        if(!user.isPresent()) {
            return new MessageResponse("Not found user with ID=" + statusDto.getUserId(), HttpStatus.NOT_FOUND, LocalDateTime.now());
        }

        Optional<User> employee = userRepository.findByIdEmployee(statusDto.getUserId());

        if(!(booking.get().getUser().getId().equals(statusDto.getUserId()) || employee.isPresent())){
            return new MessageResponse("Only user booking this space or employee change status", HttpStatus.BAD_REQUEST, LocalDateTime.now());

        }

        if(BookingStatus.COMPLETED.equals(booking.get().getStatus())) {
            return new MessageResponse("Booking has been completed, please don't change status.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(BookingStatus.CANCELLED.equals(booking.get().getStatus())) {
            return new MessageResponse("Booking has been cancelled, please don't change status.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(BookingStatus.BOOKED.equals(booking.get().getStatus()) && !BookingStatus.COMPLETED.equals(statusDto.getStatus())) {
            return new MessageResponse("Booking has been booked, only change status to completed.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        if(BookingStatus.PENDING.equals(booking.get().getStatus())
                && !(BookingStatus.CANCELLED.equals(statusDto.getStatus()) || BookingStatus.BOOKED.equals(statusDto.getStatus()))) {
            return new MessageResponse("Booking is pending, only change status to booked or cancelled.", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        }

        booking.get().setStatus(statusDto.getStatus());
        booking.get().setModifiedBy(user.get().getName());
        booking.get().setModifiedDate(new Date());

        SubSpace subSpace = booking.get().getSubSpace();

        subSpace.setModifiedBy(user.get().getName());
        subSpace.setModifiedDate(new Date());
        if(BookingStatus.COMPLETED.equals(statusDto.getStatus()) || BookingStatus.CANCELLED.equals(statusDto.getStatus())) {
            subSpace.setStatus(true);
        }
        if (BookingStatus.BOOKED.equals(statusDto.getStatus())) {
            subSpace.setStatus(false);
        }

        subSpaceRepository.save(subSpace);
        bookingRepository.save(booking.get());

        return new MessageResponse("Update booking status successfully.", HttpStatus.OK, LocalDateTime.now());
    }

    private Page<Booking> getBookings(Page<Booking> bookingPage) {
        for(Booking booking : bookingPage.getContent()) {
            booking.setUserIds(booking.getUser().getId());
            booking.setSubSpaceIds(booking.getSubSpace().getId());
        }
        return  bookingPage;
    }
}
