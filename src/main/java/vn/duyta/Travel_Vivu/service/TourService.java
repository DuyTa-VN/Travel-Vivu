package vn.duyta.Travel_Vivu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.duyta.Travel_Vivu.repository.TourRepository;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
}
