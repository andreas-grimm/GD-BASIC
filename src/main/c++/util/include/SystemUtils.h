//
// Created by Andreas Grimm on 09/11/2021.
//

#ifndef GD_BASIC_RUNTIME_SYSTEM_UTILS
#define GD_BASIC_RUNTIME_SYSTEM_UTILS

class SystemUtils {
    public:
        SystemUtils() = default;
        ~SystemUtils() = default;
        static unsigned long getFreeMemory();
        static unsigned long long getTotalSystemMemory();
};

#endif //GD_BASIC_RUNTIME_SYSTEM_UTILS
