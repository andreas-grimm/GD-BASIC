//
// Created by Andreas Grimm on 09/11/2021.
//

#ifndef GD_BASIC_RUNTIME_SYSTEM_UTILS
#define GD_BASIC_RUNTIME_SYSTEM_UTILS

class SystemUtils {
    public:
        Logger() = default;
        ~Logger() = default;
        static unsigned void info(char* strLogString);
        static unsigned void debug(char* strLogString);
        static unsigned void warn(char* strLogString);
        static unsigned void error(char* strLogString);
};

#endif //GD_BASIC_RUNTIME_SYSTEM_UTILS
